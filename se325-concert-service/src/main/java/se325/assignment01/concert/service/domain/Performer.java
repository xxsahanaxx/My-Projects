package se325.assignment01.concert.service.domain;
import se325.assignment01.concert.common.types.Genre;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;


/**
 * Author: Sahana Srinivasan
 * UPI: ssri365
 * AUID: 677618824
 */

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;


/** PERFORMER CLASS
 * This class contains the performers that are hosting their concerts at the venue, on a particular date.
 *
 * The variables for every field must match with that of the respective DTO class.
 *
 * id         the unique identifier for a performer.
 * name       the performer's name.
 * imageName  the name of an image file for the performer.
 * genre      the performer's genre.
 * blurb      the performer's description.
 *
 */

@Entity
@Table(name = "PERFORMERS")
public class Performer {

	@Id
	// Primary key field
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String name;

	@Column(name = "IMAGE_NAME")
	private String imageName;

	// Enum field - Genre is a string
	@Enumerated(EnumType.STRING)
	private Genre genre;

	@Column(columnDefinition = "TEXT")
	private String blurb;

	// Empty constructor
	public Performer() {
	}

	// Parameterised constructor
	public Performer(Long perfId, String perfName, String perfImageName, Genre perfGenre, String perfBlurb) {
		this.id = perfId;
		this.name = perfName;
		this.imageName = perfImageName;
		this.genre = perfGenre;
		this.blurb = perfBlurb;
	}

	// Override methods
	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;

		if (o == null || getClass() != o.getClass())
			return false;

		Performer that = (Performer) o;
		return new EqualsBuilder().append(id, that.id).append(name, that.name)
				.append(imageName, that.imageName).append(genre, that.genre).isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 37).append(id)
				.append(name).append(imageName).append(genre).toHashCode();
	}

	// Getter and setter functions
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}


	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}


	public String getImageName() {
		return imageName;
	}
	public void setImageName(String imageName) {
		this.imageName = imageName;
	}


	public Genre getGenre() {
		return genre;
	}
	public void setGenre(Genre genre) {
		this.genre = genre;
	}


	public String getBlurb() {
		return blurb;
	}
	public void setBlurb(String blurb) {
		this.blurb = blurb;
	}


}
