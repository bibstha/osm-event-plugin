package event_editor;

import java.sql.Date;
import java.util.Map;

public class EventEntity
{
    private Map<String, String> categories;
    private String name;
    private Date startDate;
    private Date endDate;
    private String organization;
    private String url;
    private String numOfParticipants;

    private enum repeat
    {
	YEARLY, BIMONTHLY, WEEKLY, BIWEEKLY, DAILY, ONCE
    }

    private String comment;

    public Map<String, String> getCategories()
    {
	return categories;
    }

    public void setCategories(Map<String, String> categories)
    {
	this.categories = categories;
    }

    public String getName()
    {
	return name;
    }

    public void setName(String name)
    {
	this.name = name;
    }

    public Date getStartDate()
    {
	return startDate;
    }

    public void setStartDate(Date startDate)
    {
	this.startDate = startDate;
    }

    public Date getEndDate()
    {
	return endDate;
    }

    public void setEndDate(Date endDate)
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
}
