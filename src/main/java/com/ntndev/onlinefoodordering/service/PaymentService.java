package com.ntndev.onlinefoodordering.service;

import com.ntndev.onlinefoodordering.dto.response.PaymentResponse;
import com.ntndev.onlinefoodordering.model.Order;
import com.stripe.exception.StripeException;

public interface PaymentService {

    PaymentResponse createPaymentLink(Order order) throws StripeException;
}
