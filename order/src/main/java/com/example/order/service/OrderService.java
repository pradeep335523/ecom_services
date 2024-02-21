package com.example.order.service;

import com.example.order.dto.*;
import com.example.order.entity.Order;
import com.example.order.entity.OrderLineItems;
import com.example.order.repository.OrderRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.web.reactive.function.client.WebClientAutoConfiguration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriBuilder;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Service
@Slf4j
@RequiredArgsConstructor
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Value("${app.property.inventory.service-url}")
    private String inventoryServiceUrl;

    private final WebClient.Builder webClientBuilder;

    @CircuitBreaker(name = "inventory",fallbackMethod = "inventoryFallBackMethod")
    @TimeLimiter(name = "inventory")
    @Retry(name = "inventory")
    public CompletableFuture<String> createOrder(OrderRequest orderRequest) {
        Order order = new Order();
        String orderId = UUID.randomUUID().toString();
        order.setOrderId(orderId);
        order.setOrderLineItemsList(orderRequest.getOrderLineItemsRequests().stream().map(this::mapToOrderLineItems).toList());

        List<String> skuCodeList = order.getOrderLineItemsList().stream().map(OrderLineItems::getSkuCode).toList();
        log.info("inventory service URL : {}",inventoryServiceUrl);
        // Before placing order check for stock availability, if all products are in stock then only place the order
        InventoryStockResponse[] result = webClientBuilder.build().get().uri(inventoryServiceUrl, uriBuilder ->
                uriBuilder.queryParam("skuCode", skuCodeList).build()
        ).retrieve().bodyToMono(InventoryStockResponse[].class).block();
        boolean validToPlace = Arrays.stream(result).allMatch(oneLine -> oneLine.isProductInStock());
        if (validToPlace) {
            orderRepository.save(order);
            log.info("Order Placed Successfully : {}", order.getOrderId());
            return CompletableFuture.supplyAsync(() ->  "Order Placed Successfully") ;
        }
        return CompletableFuture.supplyAsync(() -> "Unable to place Order, Try again Later... !!!");
    }

    public CompletableFuture<String> inventoryFallBackMethod(OrderRequest orderRequest, RuntimeException runtimeException)
    {
        return CompletableFuture.supplyAsync(() -> "Getting fallback response here as Original service is Not Working...!!!");
    }

    private OrderLineItems mapToOrderLineItems(OrderLineItemsRequest orderLineItemsRequest) {
        OrderLineItems lineItems = new OrderLineItems();
        lineItems.setQuantity(orderLineItemsRequest.getQuantity());
        lineItems.setPrice(orderLineItemsRequest.getPrice());
        lineItems.setSkuCode(orderLineItemsRequest.getSkuCode());
        return lineItems;
    }

    public List<OrderResponse> getAllOrders() {
        List<Order> orders = orderRepository.findAll();
        return orders.stream().map(this::mapToOrders).toList();

    }

    private OrderResponse mapToOrders(Order order) {
        OrderResponse response = new OrderResponse();
        response.setId(order.getId());
        response.setOrderId(order.getOrderId());
        response.setOrderLineItemsResponses(order.getOrderLineItemsList().stream().map(this::mapToOrderLinesResponse).toList());
        return response;
    }

    private OrderLineItemsResponse mapToOrderLinesResponse(OrderLineItems orderLineItems) {
        OrderLineItemsResponse orderLineItemsResponse = new OrderLineItemsResponse();
        orderLineItemsResponse.setId(orderLineItems.getId());
        orderLineItemsResponse.setQuantity(orderLineItems.getQuantity());
        orderLineItemsResponse.setPrice(orderLineItems.getPrice());
        orderLineItemsResponse.setSkuCode(orderLineItems.getSkuCode());
        return orderLineItemsResponse;
    }
}
