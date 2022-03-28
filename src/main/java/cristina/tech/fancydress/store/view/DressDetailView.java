package cristina.tech.fancydress.store.view;


import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;

@JsonIgnoreProperties({"ratingsCount"})
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class DressDetailView implements Serializable {

    private static final long serialVersionUID = 4635410771215L;

    private String id;
    private BigInteger ratingsCount;
    private BigDecimal price;
    private String name;
    private String season;
    private String color;
    private Short averageRating;
    private String brandName;

    public static DressDetailView map(Object[] objects) {
        DressDetailView view = new DressDetailView();
        view.setId((String) objects[0]);
        view.setRatingsCount((BigInteger) objects[1]);
        view.setName((String) objects[2]);
        view.setSeason((String) objects[3]);
        view.setColor((String) objects[4]);
        view.setPrice((BigDecimal) objects[5]);
        view.setAverageRating((Short) objects[6]);
        view.setBrandName((String) objects[7]);

        return view;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public BigInteger getRatingsCount() {
        return ratingsCount;
    }

    public void setRatingsCount(BigInteger ratingsCount) {
        this.ratingsCount = ratingsCount;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSeason() {
        return season;
    }

    public void setSeason(String season) {
        this.season = season;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Short getAverageRating() {
        return averageRating;
    }

    public void setAverageRating(Short averageRating) {
        this.averageRating = averageRating;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }
}
