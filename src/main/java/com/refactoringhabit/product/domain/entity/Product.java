package com.refactoringhabit.product.domain.entity;

import com.refactoringhabit.category.domain.entity.CategoryMiddle;
import com.refactoringhabit.common.domain.entity.BaseCreateTimeEntity;
import com.refactoringhabit.host.domain.entity.Host;
import com.refactoringhabit.product.domain.enums.ProductStatus;
import com.refactoringhabit.product.domain.enums.ProductType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;

@Table(name = "products")
@DynamicInsert
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Product extends BaseCreateTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "alt_id")
    private String altId; // 대체키

    @Column(name = "name")
    private String name;

    @Column(name = "zip_code")
    private String zipCode;

    @Column(name = "address1")
    private String address1;

    @Column(name = "address2")
    private String address2;

    @Column(name = "extra_address")
    private String extraAddress;

    @Column(name = "image_file_names")
    private String imageFileNames;

    @Column(name = "description")
    private String description;

    @Column(name = "closed_at")
    private String closedAt;

    @Column(name = "tag_gender")
    private String tagGender;

    @Column(name = "tag_age")
    private String tagAge;

    @Column(name = "tag_with")
    private String tagWith;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private ProductType type;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private ProductStatus status;

    @ManyToOne
    @JoinColumn(name = "category_middle_id")
    private CategoryMiddle categoryMiddle;

    @ManyToOne
    @JoinColumn(name = "host_id")
    private Host host;

    @OneToMany(mappedBy = "product")
    private List<Option> options;

    @Builder
    public Product(String altId, String name, String zipCode, String address1, String address2,
        String extraAddress, String imageFileNames, String description, String closedAt,
        String tagGender, String tagAge, String tagWith, ProductType type,
        CategoryMiddle categoryMiddle, Host host) {
        this.altId = altId;
        this.name = name;
        this.zipCode = zipCode;
        this.address1 = address1;
        this.address2 = address2;
        this.extraAddress = extraAddress;
        this.imageFileNames = imageFileNames;
        this.description = description;
        this.closedAt = closedAt;
        this.tagGender = tagGender;
        this.tagAge = tagAge;
        this.tagWith = tagWith;
        this.type = type;
        this.categoryMiddle = categoryMiddle;
        this.host = host;
    }
}
