package se325.assignment01.concert.service.domain;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Author: Sahana Srinivasan
 * UPI: ssri365
 * AUID: 677618824
 */

import javax.persistence.*;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

/** CONCERT CLASS
 * This class contains the concerts that are taking place at the venue, on a particular date
 * and generates a table called CONCERTS to hold the records.
 * It also has access to objects of the Performer class, to list out which Performers will be presenting
 * that concert.
 *
 * The variables of this class are the same as that of their respective DTO class.
 *
 * id           the unique identifier for a concert.
 * title        the concert's title.
 * dates        the concert's scheduled dates and times (represented as a Set of LocalDateTime instances).
 * imageName    an image name for the concert.
 * performers   the performers in the concert
 * blurb        the concert's description
 *
 */

@Entity
@Table(name = "CONCERTS")
public class Concert {

	@Id
	// Primary key is made unique
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String title;


	// Concert dates (a set of dates) are eager-fetched, which is not the default fetch method
	@ElementCollection(fetch = FetchType.EAGER)
	// The concert dates are mapped such that a concert can run on multiple dates
	@CollectionTable(name = "CONCERT_DATES", joinColumns = @JoinColumn(name = "CONCERT_ID"))
	@Column(name = "DATE")
	private Set<LocalDateTime> dates = new HashSet<>();


	@Column(name = "IMAGE_NAME")
	private String imageName;


	// Since one performer can have many concerts, as well as one concert can have multiple performers,
	// a many-to-many relationship can be established between the two domain classes
	@ManyToMany(cascade = CascadeType.PERSIST)
	@Fetch(FetchMode.SUBSELECT)
	@JoinTable(name = "CONCERT_PERFORMER", joinColumns = @JoinColumn(name = "CONCERT_ID"),
			inverseJoinColumns = @JoinColumn(name = "PERFORMER_ID"))
	private Set<Performer> performers = new HashSet<>();


	// Blurb as a text-field definition can hold longer strings
	@Column(columnDefinition = "TEXT")
	private String blurb;


	// Empty constructor
	public Concert() {

	}

	// Parameterised constructor
	public Concert(Long concId, String concTitle, String concImageName, String concBlurb) {
		this.id = concId;
		this.title = concTitle;
		this.imageName = concImageName;
		this.blurb = concBlurb;
	}

	// Override methods for domain class
	@Override
	public int hashCode() {
		return Objects.hash(dates, id, title);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof Concert)) {
			return false;
		}
		Concert other = (Concert) obj;
		return Objects.equals(dates, other.dates) && Objects.equals(id, other.id) && Objects.equals(title, other.title);
	}


	// Getter and setter methods for each field in the domain class
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}


	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}


	public Set<LocalDateTime> getDates() {
		return dates;
	}
	public void setDates(Set<LocalDateTime> dates) {
		this.dates = dates;
	}

	public Set<Performer> getPerformers() {
		return performers;
	}
	public void setPerformers(Set<Performer> performers) {
		this.performers = performers;
	}


	public String getImageName() {
		return imageName;
	}
	public void setImageName(String imageName) {
		this.imageName = imageName;
	}


	public String getBlurb() {
		return blurb;
	}
	public void setBlurb(String blurb) {
		this.blurb = blurb;
	}

}
