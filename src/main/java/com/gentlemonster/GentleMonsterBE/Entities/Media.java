package com.gentlemonster.GentleMonsterBE.Entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "media")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Media {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "image_url", length = 500) // Trường image_url là địa chỉ ảnh (thường là URL trên Cloudinary)
    @JsonProperty("image_url")
    private String imageUrl;

    @Column(name = "public_id", length = 500) // Trường public_id là ID công khai của ảnh trên Cloudinary
    private String publicId;


    // Trường alt_text là mô tả ảnh, thường dùng để cải thiện SEO và accessibility, Trợ năng (screen reader)
    @Column(name = "alt_text", length = 500)
    private String altText;

    // THUMBNAIL: ảnh nhỏ (thumbnail) hiển thị trong danh sách.
    // GALLERY: ảnh chi tiết trong trang sản phẩm.
    // BANNER: ảnh lớn hiển thị trên trang chủ hoặc trang danh mục.
    // STORY: ảnh trong các câu chuyện (stories) hoặc bài viết.
    // CITY: ảnh đại diện cho thành phố.
    // SLIDER: ảnh trong slider hoặc carousel.
    // STORE: ảnh liên quan đến cửa hàng.
    // USER: ảnh đại diện của người dùng (user profile).
    @Column(name = "type", length = 300)
    private String type;
    
    @CreationTimestamp
    @Column(name = "created_date")
    private LocalDateTime createdDate;

    @CreationTimestamp
    @Column(name = "modified_date")
    private LocalDateTime  modifiedDate;

    @ManyToOne
    @JoinColumn(name = "product_id")
    @JsonIgnore
    private Product product;

    @ManyToOne
    @JoinColumn(name = "story_id")
    @JsonIgnore
    private Story story;

    @ManyToOne
    @JoinColumn(name = "banner_id")
    @JsonIgnore
    private Slider banner;

    @ManyToOne
    @JoinColumn(name = "city_id")
    @JsonIgnore
    private City city;

    @ManyToOne
    @JoinColumn(name = "slider_id")
    @JsonIgnore
    private Slider slider;

    @ManyToOne
    @JoinColumn(name = "store_id")
    @JsonIgnore
    private Store store;

    @OneToOne
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;
}
