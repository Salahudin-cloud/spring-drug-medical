package com.example.drugmed.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "invoice_list_items")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InvoiceListItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


}
