package domain;

public class Category {

	private String name;
	private String slug;
	private String image;
	private boolean imageContainer;
	
	public boolean isImageContainer() {
		return imageContainer;
	}

	public void setImageContainer(boolean imageContainer) {
		this.imageContainer = imageContainer;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSlug() {
		return slug;
	}

	public void setSlug(String slug) {
		this.slug = slug;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

}
