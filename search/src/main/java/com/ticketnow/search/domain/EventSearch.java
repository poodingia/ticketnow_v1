package com.ticketnow.search.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Document(indexName = "connector-event")
public class EventSearch {
    @Id
    private String index;

    @Field(name = "public_events_id", type = FieldType.Long)
    private Long id;

    @Field(name = "public_events_title",type = FieldType.Text , analyzer = "autocomplete_index", searchAnalyzer = "autocomplete_search")
    private String title;

    @Field(name ="public_events_bg_image_path", type = FieldType.Text)
    private String bgImagePath;

    @Field(name = "public_events_description", type = FieldType.Text)
    private String description;

    @Field(name = "public_events_date", type = FieldType.Date)
    private String date;

    @Field(name = "public_events_category", type = FieldType.Text)
    private String category;

    @Field(name = "public_events_location", type = FieldType.Text)
    private String location;

    @Field(name = "public_events_is_approved", type = FieldType.Boolean)
    private Boolean approved;

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBgImagePath() {
        return bgImagePath;
    }

    public void setBgImagePath(String bgImagePath) {
        this.bgImagePath = bgImagePath;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Boolean getApproved() {
        return approved;
    }

    public void setApproved(Boolean approved) {
        this.approved = approved;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "EventSearch{" +
                "index='" + index + '\'' +
                ", id=" + id +
                ", title='" + title + '\'' +
                ", bgImagePath='" + bgImagePath + '\'' +
                ", description='" + description + '\'' +
                ", date='" + date + '\'' +
                ", category='" + category + '\'' +
                ", location='" + location + '\'' +
                ", approved=" + approved +
                '}';
    }
}
