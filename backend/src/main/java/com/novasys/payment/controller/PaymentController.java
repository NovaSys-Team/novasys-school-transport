
package com.novasys.payment.controller;

import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;
import com.novasys.payment.repository.PaymentTransactionRepository;
import java.util.List;
import com.novasys.payment.entity.PaymentTransaction;
import com.novasys.payment.service.PaymentService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/demo/payments")
public class PaymentController {

    private final PaymentService paymentService;
private final PaymentTransactionRepository transactionRepository;

    public PaymentController(
        PaymentService paymentService,
        PaymentTransactionRepository transactionRepository) {

    this.paymentService = paymentService;
    this.transactionRepository = transactionRepository;
}
    @PostMapping("/{invoiceId}")
    public ResponseEntity<PaymentTransaction> makeDemoPayment(
            @PathVariable Long invoiceId,
            @RequestParam boolean simulateSuccess) {

       PaymentTransaction transaction;

try {
    transaction = paymentService.processDemoPayment(
            invoiceId, simulateSuccess);
} catch (IllegalStateException ex) {
    throw new ResponseStatusException(
            HttpStatus.CONFLICT, ex.getMessage(), ex);
}

        return ResponseEntity.ok(transaction);
    }
    
@GetMapping("/history/{invoiceId}")
public ResponseEntity<List<PaymentTransaction>> getPaymentHistory(
        @PathVariable Long invoiceId) {

    List<PaymentTransaction> transactions =
            transactionRepository.findByInvoiceId(invoiceId);

    return ResponseEntity.ok(transactions);
}

}
