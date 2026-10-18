
package com.novasys.payment.controller;

import com.novasys.payment.entity.Invoice;
import com.novasys.payment.entity.InvoiceStatus;
import com.novasys.payment.repository.InvoiceRepository;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/demo/invoices")
public class InvoiceController {

    private final InvoiceRepository invoiceRepository;

    public InvoiceController(InvoiceRepository invoiceRepository) {
        this.invoiceRepository = invoiceRepository;
    }

    @PostMapping
    public ResponseEntity<Invoice> createInvoice(
            @RequestBody Invoice invoice) {

        if (invoice.getParentId() == null ||
                invoice.getDriverId() == null ||
                invoice.getStudentId() == null ||
                invoice.getAmount() == null ||
                invoice.getAmount().signum() <= 0 ||
                invoice.getDueDate() == null) {
            return ResponseEntity.badRequest().build();
        }

        invoice.setStatus(InvoiceStatus.PENDING);

        Invoice savedInvoice = invoiceRepository.save(invoice);

        return ResponseEntity.ok(savedInvoice);
    }

    @GetMapping
    public List<Invoice> getAllInvoices() {
        return invoiceRepository.findAll();
    }

    @GetMapping("/{invoiceId}")
    public ResponseEntity<Invoice> getInvoice(
            @PathVariable Long invoiceId) {

        return invoiceRepository.findById(invoiceId)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
