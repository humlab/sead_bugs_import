package se.sead.bugsimport.site.seadmodel;

import se.sead.bugsimport.sitelocations.seadmodel.SiteLocation;
import se.sead.sead.model.LoggableEntity;
import se.sead.utils.BigDecimalDefinition;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name="tbl_sites")
@SequenceGenerator(name="site_id_seq", sequenceName = "tbl_sites_site_id_seq")
public class SeadSite extends LoggableEntity implements Comparable<SeadSite>{

    @Id
    @GeneratedValue(generator = "site_id_seq", strategy = GenerationType.IDENTITY)
    @Column(name="site_id", nullable = false)
    private Integer id;
    @Column(name="altitude")
    private BigDecimal altitude;
    @Column(name="latitude_dd")
    private BigDecimal latitude;
    @Column(name="longitude_dd")
    private BigDecimal longitude;
    @Column(name="national_site_identifier")
    private String nationalSiteIdentifier;
    @Column(name="site_description")
    private String description;
    @Column(name="site_name")
    private String name;
    @OneToMany(mappedBy = "site")
    private List<SiteLocation> siteLocations;

    @Override
    public Integer getId() {
        return id;
    }

    protected void setId(Integer id) {
        this.id = id;
    }

    public BigDecimal getAltitude() {
        return altitude;
    }

    public void setAltitude(BigDecimal altitude) {
        this.altitude = altitude;
    }

    public BigDecimal getLatitude() {
        return latitude;
    }

    public void setLatitude(BigDecimal latitude) {
        this.latitude = latitude;
    }

    public BigDecimal getLongitude() {
        return longitude;
    }

    public void setLongitude(BigDecimal longitude) {
        this.longitude = longitude;
    }

    public String getNationalSiteIdentifier() {
        return nationalSiteIdentifier;
    }

    public void setNationalSiteIdentifier(String nationalSiteIdentifier) {
        this.nationalSiteIdentifier = nationalSiteIdentifier;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<SiteLocation> getSiteLocations() {
        return siteLocations;
    }

    public void setSiteLocations(List<SiteLocation> siteLocations) {
        this.siteLocations = siteLocations;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || !(o instanceof SeadSite)) return false;

        SeadSite seadSite = (SeadSite) o;

        if (id != null && seadSite.id != null && !id.equals(seadSite.id)) return false;
        if (!BigDecimalDefinition.equalBigDecimalNumericValues(altitude, seadSite.altitude)) {
            return false;
        }
        if (!BigDecimalDefinition.equalBigDecimalNumericValues(latitude, seadSite.latitude)) {
            return false;
        }
        if (!BigDecimalDefinition.equalBigDecimalNumericValues(longitude, seadSite.longitude)) {
            return false;
        }
        if (nationalSiteIdentifier != null ? !nationalSiteIdentifier.equals(seadSite.nationalSiteIdentifier) : seadSite.nationalSiteIdentifier != null)
            return false;
        if (description != null && seadSite.description != null  ?
                !description.replace("\r\n", "\n").equals(seadSite.description.replace("\r\n", "\n")) :
                (description == null && seadSite.description != null || description != null && seadSite.description == null))
            return false;
        return name != null ? name.equals(seadSite.name) : seadSite.name == null;

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (altitude != null ? altitude.hashCode() : 0);
        result = 31 * result + (latitude != null ? latitude.hashCode() : 0);
        result = 31 * result + (longitude != null ? longitude.hashCode() : 0);
        result = 31 * result + (nationalSiteIdentifier != null ? nationalSiteIdentifier.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "SeadSite{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", altitude=" + altitude +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", nationalSiteIdentifier='" + nationalSiteIdentifier + '\'' +
                ", description='" + description + '\'' +
                ", dateUpdated='" + getDateUpdated() + '\'' +
                '}';
    }

    @Override
    public int compareTo(SeadSite o) {
        return getName().compareTo(o.getName());
    }
}
