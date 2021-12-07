package org.prateekgupta.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;


@javax.persistence.Entity
@Getter
@Setter
@ToString
@Table
@NamedQueries({
        @NamedQuery(name = "getByPrice",
                query = "from MobileEntity where price<:maxPrice and price>:minPrice"),
        @NamedQuery(name = "getByBrandName", query = "from MobileEntity where brandName=:providedBrandName"),
        @NamedQuery(name = "updatePriceByModelNumber",
                query = "update MobileEntity set price=:providedPrice where modelNumber=:providedModelNumber"),
        @NamedQuery(name = "getByModelNumber",
                query = "from MobileEntity where modelNumber=:providedModelNumber"),
        @NamedQuery(name = "updateAvailabilityByModelName",
                query = "update MobileEntity set availability=:providedAvailability where modelName=:providedModelName"),
        @NamedQuery(name = "getByModelName",
                query = "from MobileEntity where modelName=:providedModelName"),
})
public class MobileEntity {
    @Id
    @GenericGenerator(name = "autoincrement", strategy = "increment")
    @GeneratedValue(generator = "autoincrement")
    private int id;
    private String brandName;
    private int modelNumber;
    private String modelName;
    private String type;
    private int ram;
    private int rom;
    private int price;
    private String availability;
}
