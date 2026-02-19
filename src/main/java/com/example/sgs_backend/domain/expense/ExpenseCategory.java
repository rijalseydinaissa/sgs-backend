package com.example.sgs_backend.domain.expense;

import com.example.sgs_backend.domain.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.*;

/**
 * ✅ extends BaseEntity
 * Catégorie arborescente (comme ProductCategory).
 */
@SuperBuilder
@Entity
@Table(name = "expense_categories", indexes = {
    @Index(name = "idx_exp_cat_code", columnList = "code"),
    @Index(name = "idx_exp_cat_parent", columnList = "parent_id")
})
@Getter @Setter @NoArgsConstructor
public class ExpenseCategory extends BaseEntity {

    @Column(nullable = false, unique = true, length = 50)
    private String code;

    @Column(nullable = false, length = 150)
    private String name;

    @Column(length = 300)
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private ExpenseCategory parent;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL)
    @Builder.Default
    private List<ExpenseCategory> children = new ArrayList<>();

    @Column(nullable = false)
    private boolean active = true;

    public ExpenseCategory(String code, String name, ExpenseCategory parent) {
        this.code = code; this.name = name; this.parent = parent;
    }

    public String getFullPath() {
        if (parent == null) return name;
        return parent.getFullPath() + " > " + name;
    }
}
