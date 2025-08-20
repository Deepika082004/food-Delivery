package com.example.fooddeliveryproject.controller;

import com.example.fooddeliveryproject.RequestBean.BillRequestBean;
import com.example.fooddeliveryproject.ResponseBean.BillResponseBean;
import com.example.fooddeliveryproject.service.BillService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/bill")
public class BillController {
    private final BillService billService;
    @PostMapping
    public ResponseEntity<BillResponseBean> createBill(
            @Valid @RequestBody BillRequestBean request) {
        return ResponseEntity.ok(billService.createBill(request));
    }

    @GetMapping
    public PageImpl<BillResponseBean> getAllBills(Pageable pageable) {
        return billService.getAllBills(pageable);
    }


    @GetMapping("/{id}")
    public ResponseEntity<BillResponseBean> getBillById(@PathVariable UUID id) {
        return ResponseEntity.ok(billService.getBillById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<BillResponseBean> updateBill(
            @PathVariable UUID id,
            @Valid @RequestBody BillRequestBean request) {
        return ResponseEntity.ok(billService.updateBill(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteBill(@PathVariable UUID id) {
        billService.deleteBill(id);
        return ResponseEntity.ok("Bill deleted successfully");
    }

}
