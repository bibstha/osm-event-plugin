package event_editor;

import static org.openstreetmap.josm.tools.I18n.tr;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerDateModel;
import javax.swing.SpinnerModel;

import com.toedter.calendar.JCalendar;

public class JDateTimePicker extends JPanel
{
	private static final long serialVersionUID = 5073423732802828161L;

	private String mDateOnly = "";
	private String mTimeOnly = "";
	private Date mDate = null;
	private final JCalendar mCalendar = new JCalendar();
	private JSpinner mTimeSpinner = null;
	private final JButton mSaveButton = new JButton(tr("Save"));
	private final JButton mCancelButton = new JButton(tr("Cancel"));

	private ActionListener mSaveBtnActionListener = null;
	private ActionListener mCancelBtnActionListener = null;

	public JDateTimePicker()
	{
		super();
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

		add(new JLabel(tr("Select Date")));
		add(mCalendar);

		add(new JLabel(tr("Select Time")));

		SpinnerModel model = new SpinnerDateModel();
		mTimeSpinner = new JSpinner(model);
		JComponent editor = new JSpinner.DateEditor(mTimeSpinner, "HH:mm");
		mTimeSpinner.setEditor(editor);

		add(mTimeSpinner);

		// Lay out the buttons from left to right.
		JPanel buttonPane = new JPanel();
		buttonPane.setLayout(new BoxLayout(buttonPane, BoxLayout.LINE_AXIS));
		buttonPane.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
		buttonPane.add(Box.createHorizontalGlue());
		buttonPane.add(mSaveButton);
		buttonPane.add(Box.createRigidArea(new Dimension(10, 0)));
		buttonPane.add(mCancelButton);

		add(buttonPane, BorderLayout.SOUTH);

		mSaveButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent event)
			{
				Date dateOnly = mCalendar.getDate();
				mDateOnly = new SimpleDateFormat("dd.MM.yyyy").format(dateOnly);

				Date timeOnly = (Date) mTimeSpinner.getValue();
				mTimeOnly = new SimpleDateFormat("HH:mm").format(timeOnly);

				if (mSaveBtnActionListener != null)
				{
					mSaveBtnActionListener.actionPerformed(event);
				}
			}
		});

		mCancelButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent event)
			{
				if (mCancelBtnActionListener != null)
				{
					mCancelBtnActionListener.actionPerformed(event);
				}

			}
		});
	}

	public void setDate(Date date)
	{
		mDate = date;
	}

	public String getStringDate()
	{
		StringBuilder date = new StringBuilder(17);
		if (mDateOnly != null)
		{
			date.append(mDateOnly);
		}
		if (mTimeOnly != null && !mTimeOnly.equals(""))
		{
			if (date.toString().equals(""))
			{
				date.append(mTimeOnly);
			}
			else
			{
				date.append("-").append(mTimeOnly);
			}
		}
		return date.toString();

	}

	public void setSaveBtnActionListener(ActionListener listener)
	{
		mSaveBtnActionListener = listener;
	}

	public void setCancelBtnActionListener(ActionListener listener)
	{
		mCancelBtnActionListener = listener;
	}
}