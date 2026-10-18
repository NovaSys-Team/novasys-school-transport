
package com.novasys.payment.repository;

import com.novasys.payment.entity.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InvoiceRepository extends JpaRepository<Invoice, Long> {

    List<Invoice> findByParentId(Long parentId);

    List<Invoice> findByDriverId(Long driverId);
}
