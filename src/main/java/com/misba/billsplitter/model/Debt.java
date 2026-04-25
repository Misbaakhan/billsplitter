package com.misba.billsplitter.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "debts")
public class Debt {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "debtor_id")
    private User debtor;

    @ManyToOne
    @JoinColumn(name = "creditor_id")
    private User creditor;

    @ManyToOne
    @JoinColumn(name = "group_id")
    private Group group;

    @Column(nullable = false)
    private Double amount;

    @Column(nullable = false)
    private Boolean settled = false;
}