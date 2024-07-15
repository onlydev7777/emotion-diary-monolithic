package com.example.emotiondiarymember.entity.auth;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpMethod;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "RESOURCES")
@Entity
public class Resources {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "resources_name")
  private String resourceName;

  @Column(name = "http_method")
  private HttpMethod httpMethod;

  @OneToMany(mappedBy = "resources", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<RoleResources> roleResources = new ArrayList<>();

  @Builder
  public Resources(String resourceName, HttpMethod httpMethod) {
    this.resourceName = resourceName;
    this.httpMethod = httpMethod;
  }

  public static Resources of(String resourceName, HttpMethod httpMethod) {
    return new Resources(resourceName, httpMethod);
  }
}
