package com.hua.api.entity;

import com.hua.api.enums.EventTypeEnum;
import com.hua.api.enums.MyPostgreSQLEnumType;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "HUA_EVENT")
@TypeDef(name = "pgsql_enum", typeClass = MyPostgreSQLEnumType.class)
public class HuaEvent implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Type(type = "pgsql_enum")
    @Column(name = "event_type")
    private EventTypeEnum eventType;

    @Column(name = "is_admin_informed")
    private boolean adminInformed;

    @CreatedDate
    @Column(name = "created_date", nullable = false)
    private LocalDateTime createdDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID", referencedColumnName = "ID")
    private HuaUser huaUser;

    public HuaEvent() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public EventTypeEnum getEventType() {
        return eventType;
    }

    public void setEventType(EventTypeEnum eventType) {
        this.eventType = eventType;
    }

    public boolean isAdminInformed() {
        return adminInformed;
    }

    public void setAdminInformed(boolean adminInformed) {
        this.adminInformed = adminInformed;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public HuaUser getHuaUser() {
        return huaUser;
    }

    public void setHuaUser(HuaUser huaUser) {
        this.huaUser = huaUser;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HuaEvent huaEvent = (HuaEvent) o;
        return Objects.equals(id, huaEvent.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
