package com.example.drugmed.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Drug {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(mappedBy = "drug", cascade = CascadeType.ALL, orphanRemoval = true)
    private DrugDetail drugDetail;

    @ManyToMany(mappedBy = "drugs")
    private List<Prescription> prescriptions;

    @Column(nullable = false, name = "drug_name")
    private String drugName;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DrugCategory category;

    @Column(nullable = false)
    private int price;

    @Column(nullable = false)
    private int stock;

    @CreationTimestamp
    @Column(nullable = false, name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false, name = "updated_at")
    @UpdateTimestamp
    private LocalDateTime updatedAt;
    public enum DrugCategory {
        TABLET, SYRUP, CAPSULE
    }
}
