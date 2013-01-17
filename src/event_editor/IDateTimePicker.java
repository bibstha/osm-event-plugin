package event_editor;

import java.awt.event.ActionListener;
import java.util.Date;

public interface IDateTimePicker
{
	public void setDate(Date date);

	/**
	 * It might not necessarily be a valid and formatted date, allow using
	 * String
	 * 
	 * @return
	 */
	public String getDate();

	public void setOnSaveActionListener(ActionListener listener);

	public void setOnCancelActionListener(ActionListener listener);
}
