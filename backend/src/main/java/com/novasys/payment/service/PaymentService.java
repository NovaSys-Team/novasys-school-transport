
package com.novasys.payment.service;

import com.novasys.payment.entity.*;
import com.novasys.payment.repository.InvoiceRepository;
import com.novasys.payment.repository.PaymentTransactionRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class PaymentService {

    private final InvoiceRepository invoiceRepository;
    private final PaymentTransactionRepository transactionRepository;

    public PaymentService(
            InvoiceRepository invoiceRepository,
            PaymentTransactionRepository transactionRepository) {
        this.invoiceRepository = invoiceRepository;
        this.transactionRepository = transactionRepository;
    }

    @Transactional
    public PaymentTransaction processDemoPayment(
            Long invoiceId, boolean simulateSuccess) {

        Invoice invoice = invoiceRepository.findById(invoiceId)
                .orElseThrow(() ->
                        new IllegalArgumentException("Invoice not found"));

        if (invoice.getStatus() == InvoiceStatus.PAID) {
            throw new IllegalStateException("Invoice already paid");
        }

        if (invoice.getStatus() == InvoiceStatus.CANCELLED) {
            throw new IllegalStateException("Invoice cancelled");
        }

        if (invoice.getAmount() == null ||
                invoice.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Invalid invoice amount");
        }

        PaymentTransaction transaction = new PaymentTransaction();
        transaction.setInvoiceId(invoice.getId());
        transaction.setAmount(invoice.getAmount());

        if (simulateSuccess) {
            transaction.setStatus(PaymentStatus.SUCCESS);
            invoice.setStatus(InvoiceStatus.PAID);
            invoiceRepository.save(invoice);
        } else {
            transaction.setStatus(PaymentStatus.FAILED);
        }

        return transactionRepository.save(transaction);
    }
}
