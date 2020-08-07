package net.chrzastek.moto.cars;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import net.chrzastek.moto.users.User;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "cars")
@RequiredArgsConstructor
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Car {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  @ManyToOne
  private User user;

  @NonNull
  private String brandname;

  @NonNull
  private String modelname;

  @NonNull
  private int manufactureyear;

  public Car(User user, String brandname, String modelname, int manufactureyear) {
    this.user = user;
    this.brandname = brandname;
    this.modelname = modelname;
    this.manufactureyear = manufactureyear;
  }

}