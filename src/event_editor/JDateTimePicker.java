package event_editor;

import static org.openstreetmap.josm.tools.I18n.tr;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerDateModel;
import javax.swing.SpinnerModel;

import com.toedter.calendar.JCalendar;

public class JDateTimePicker extends JComponent implements IDateTimePicker
{
	private static final long serialVersionUID = 5073423732802828161L;

	private static final String SAVEBUTTON_LABEL = "Update";
	private static final String CANCELBUTTON_LABEL = "Cancel";

	private Date mDate = null;
	private String mDateString = null;
	private static final String DATE_FORMAT = "dd.MM.yyyy-HH:mm";

	private final JTextField mDateTextField = new JTextField();
	private final JButton mOpenCalendarButton = new JButton(tr("..."));
	private final JCalendar mCalendar = new JCalendar();
	private JSpinner mTimeSpinner = null;
	private final JButton mSaveButton = new JButton(tr(SAVEBUTTON_LABEL));
	private final JButton mCancelButton = new JButton(tr(CANCELBUTTON_LABEL));
	private final Window mComponentOwnerFrame;
	private final JPickerDialog mPicker;

	private ActionListener mOnSaveActionListener = null;
	private ActionListener mOnCancelActionListener = null;

	public JDateTimePicker(Window window)
	{
		super();
		mComponentOwnerFrame = window;
		mPicker = new JPickerDialog(window);
		setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));

		mOpenCalendarButton.setPreferredSize(new Dimension(20, 20));
		add(mDateTextField);
		add(mOpenCalendarButton);

		mOpenCalendarButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				if (mPicker == null)
				{
					return;
				}

				if (mPicker.isVisible())
				{
					closePicker();
				}
				else
				{
					openPicker();
				}
			}
		});
	}

	@Override
	public void setDate(Date date)
	{
		mDate = date;
		updateComponents();
	}

	public void setDate(String date)
	{
		if (date != null)
		{
			mDateString = date;
			DateFormat df = new SimpleDateFormat(DATE_FORMAT);
			try
			{
				Date dt = df.parse(date);
				mDate = dt;
			}
			catch (ParseException e)
			{
				mDate = null;
			}
			updateComponents();
		}
	}

	@Override
	public String getDate()
	{
		return mDateTextField.getText();
	}

	@Override
	public void setOnSaveActionListener(ActionListener listener)
	{
		mOnSaveActionListener = listener;
	}

	@Override
	public void setOnCancelActionListener(ActionListener listener)
	{
		mOnCancelActionListener = listener;
	}

	public Component addLeftAligned(JComponent comp)
	{
		comp.setAlignmentX(LEFT_ALIGNMENT);
		return super.add(comp);
	}

	/**
	 * Update visible components
	 */
	private void updateComponents()
	{
		if (mDate == null)
		{
			mDateTextField.setText(mDateString);
		}
		else
		{
			mDateTextField.setText(new SimpleDateFormat(DATE_FORMAT).format(mDate));
			mCalendar.setDate(mDate);
			if (mTimeSpinner != null)
			{
				mTimeSpinner.setValue(mDate);
			}
		}
	}

	protected void closePicker()
	{
		if (mPicker != null)
		{
			mPicker.setVisible(false);
		}
	}

	protected void openPicker()
	{
		if (mPicker != null)
		{
			Point dialogLocation = mDateTextField.getLocationOnScreen();
			dialogLocation.y += mDateTextField.getHeight();
			mPicker.setLocation(dialogLocation);
			mPicker.pack();
			mPicker.setVisible(true);
		}

	}

	protected class JPickerDialog extends JDialog
	{
		private static final long serialVersionUID = -213643491775097974L;

		private final JPanel panel = new JPanel();

		public JPickerDialog(Window window)
		{
			super(window);
			setUndecorated(true);

			add(panel);

			panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
			panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

			// Date
			JLabel jlSelectDate = new JLabel(tr("Choose Date"));
			addPanelComponent(jlSelectDate);
			addPanelComponent(mCalendar);

			// Time
			JLabel jlSelectTime = new JLabel(tr("Choose Time"));
			addPanelComponent(jlSelectTime);

			SpinnerModel model = new SpinnerDateModel();
			mTimeSpinner = new JSpinner(model);
			JComponent editor = new JSpinner.DateEditor(mTimeSpinner, "HH:mm");
			mTimeSpinner.setEditor(editor);
			addPanelComponent(mTimeSpinner);

			// Save and Cancel buttons
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new BoxLayout(buttonPane, BoxLayout.LINE_AXIS));
			buttonPane.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));
			buttonPane.add(Box.createHorizontalGlue());
			buttonPane.add(mSaveButton);
			buttonPane.add(Box.createRigidArea(new Dimension(10, 0)));
			buttonPane.add(mCancelButton);
			addPanelComponent(buttonPane);

			mSaveButton.addActionListener(new ActionListener()
			{
				@Override
				public void actionPerformed(ActionEvent event)
				{
					// Update mDate
					Date calendarDate = mCalendar.getDate();
					Date timespinnerDate = (Date) mTimeSpinner.getValue();

					Calendar c1 = Calendar.getInstance();
					c1.setTime(calendarDate);

					Calendar c2 = Calendar.getInstance();
					c2.setTime(timespinnerDate);

					c1.set(Calendar.HOUR_OF_DAY, c2.get(Calendar.HOUR_OF_DAY));
					c1.set(Calendar.MINUTE, c2.get(Calendar.MINUTE));

					mDate = c1.getTime();
					updateComponents();

					if (mOnSaveActionListener != null)
					{
						mOnSaveActionListener.actionPerformed(event);
					}
					setVisible(false);

				}
			});

			mCancelButton.addActionListener(new ActionListener()
			{
				@Override
				public void actionPerformed(ActionEvent event)
				{
					if (mOnCancelActionListener != null)
					{
						mOnCancelActionListener.actionPerformed(event);
					}
					setVisible(false);
				}
			});
		}

		protected Component addPanelComponent(JComponent component)
		{
			component.setAlignmentX(LEFT_ALIGNMENT);
			return panel.add(component);
		}
	}
}