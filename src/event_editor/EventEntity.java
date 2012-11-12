package event_editor;

public class EventEntity
{
    private String category;
    private String subCategory;
    private String name;
    private String startDate;
    private String endDate;
    private String organization;
    private String url;
    private String numOfParticipants;
    private String howOften;
    private String howOftenOther;
    private String comment;

    private boolean toBeDeleted = false;

    public String getCategory()
    {
	return category;
    }

    public void setCategory(String category)
    {
	this.category = category;
    }

    public String getSubCategory()
    {
	return subCategory;
    }

    public void setSubCategory(String subCategory)
    {
	this.subCategory = subCategory;
    }

    public String getHowOften()
    {
	return howOften;
    }

    public void setHowOften(String howOften)
    {
	this.howOften = howOften;
    }

    public String getHowOftenOther()
    {
	return howOftenOther;
    }

    public void setHowOftenOther(String howOftenOther)
    {
	this.howOftenOther = howOftenOther;
    }

    public String getName()
    {
	return name;
    }

    public void setName(String name)
    {
	this.name = name;
    }

    public String getStartDate()
    {
	return startDate;
    }

    public void setStartDate(String startDate)
    {
	this.startDate = startDate;
    }

    public String getEndDate()
    {
	return endDate;
    }

    public void setEndDate(String endDate)
    {
	this.endDate = endDate;
    }

    public String getOrganization()
    {
	return organization;
    }

    public void setOrganization(String organization)
    {
	this.organization = organization;
    }

    public String getUrl()
    {
	return url;
    }

    public void setUrl(String url)
    {
	this.url = url;
    }

    public String getNumOfParticipants()
    {
	return numOfParticipants;
    }

    public void setNumOfParticipants(String numOfParticipants)
    {
	this.numOfParticipants = numOfParticipants;
    }

    public String getComment()
    {
	return comment;
    }

    public void setComment(String comment)
    {
	this.comment = comment;
    }

    @Override
    public String toString()
    {
	return String.format("%s %s %s %s %s", name, organization, category, subCategory, url);
    }

    public boolean isToBeDeleted()
    {
	return toBeDeleted;
    }

    public void setToBeDeleted(boolean toBeDeleted)
    {
	this.toBeDeleted = toBeDeleted;
    }
}
