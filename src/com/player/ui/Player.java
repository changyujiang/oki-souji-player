package com.player.ui;

import com.player.entity.Frame;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Map;

import static com.player.ui.Utils.*;
import static java.awt.BorderLayout.*;

public class Player implements ChangeListener {

    private final int FPS = 30;
    private final int mDelay = 33;

    private File mCurrentDir;

    private JFrame mJFrame = new JFrame();
    private JLabel mVideoLabel = new JLabel();
    private JLabel mFrameLabel = new JLabel();
    private JSlider mSlider = new JSlider(JSlider.HORIZONTAL,
            1, 9000, 1);
    private int mCurrentProgress = 0;
    private Map<Integer, Frame> mFrameMap = null;

    private Font font = new Font("Serif", Font.PLAIN, 15);

    private ActionListener updater = evt -> {
        if (mCurrentProgress > 9000) {
            mCurrentProgress = 1;
        }
        updateFrame();
    };

    private Timer primaryTimer = new Timer(mDelay, updater);

    public static void main(String[] args) {
        new Player();
        log("Oki-Player!");
    }

    private Player() {
        initUi();
    }

    private void initUi() {
        initJFrame();

        JPanel actionPanel = initActionPanel();
        JPanel videoPanel = initVideo();
        mJFrame.add(actionPanel, PAGE_START);
        mJFrame.add(videoPanel, LINE_START);
        mJFrame.setVisible(true);
    }

    private void initJFrame() {
        mJFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        mJFrame.setTitle("Oki Player");
        mJFrame.setSize(500,388);
        mJFrame.setLocationRelativeTo(null);
    }

    private JPanel initActionPanel() {
        JPanel panel = new JPanel();
        panel.setSize(500, 100);
        FlowLayout layout = new FlowLayout();
        panel.setLayout(layout);

        setupActionList(panel);
        setupButtons(panel);
        return panel;
    }

    private void setupActionList(JPanel panel) {
        JLabel actionLabel = new JLabel("Action:");
        String[] actionOptions = {
                "Import Video",
                "Go Back"
        };
        JList<String> actionList = new JList<>(actionOptions);
        actionList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        actionList.setLayoutOrientation(JList.VERTICAL);

        MouseListener mouseListener = new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 1) {
                    int index = actionList.getSelectedIndex();
                    if (index == 0){
                        importVideo();
                    } else if (index == 1) {
                        goBack();
                    }
                }
            }
        };
        actionList.addMouseListener(mouseListener);

        panel.add(actionLabel);
        panel.add(actionList);
    }

    private void importVideo() {
        mCurrentDir = selectFile(mJFrame);
        mFrameMap = loadFrameMeta(mCurrentDir);
        mCurrentProgress = 1;
        startPlay();
    }

    private void goBack() {

    }

    private void navigateTo(String folderPath, int frameNumber) {
        mCurrentDir = new File(folderPath);
//        mCurrentLinks = loadLinks(mCurrentDir);
        mCurrentProgress = frameNumber;
        startPlay();
    }

    private void startPlay() {
        primaryTimer.start();
    }

    private void updateFrame() {
        BufferedImage frame = loadFrame(mCurrentDir, mCurrentProgress);
        if (frame == null) {
            log("Error: frame is null.");
        } else {
            mVideoLabel.setIcon(new ImageIcon(frame));
            mFrameLabel.setText("Playing Frame " + mCurrentProgress);
            if (mCurrentProgress % 100 - 1 == 0) {
                mSlider.setValue(mCurrentProgress);
            }
            mCurrentProgress++;
        }
    }

    private void setupButtons(JPanel panel) {
        JButton resume = new JButton("Play/Resume");
        resume.addActionListener(e -> {
            if (!primaryTimer.isRunning()) {
                primaryTimer.start();
            }
        });
        panel.add(resume);

        JButton pause = new JButton("Pause");
        pause.addActionListener(e -> {
            if (primaryTimer.isRunning()) {
                primaryTimer.stop();
            }
        });
        panel.add(pause);

        JButton stop = new JButton("Stop");
        stop.addActionListener(e -> {
            if (primaryTimer.isRunning()) {
                primaryTimer.stop();
            }
            mCurrentProgress = 1;
            updateFrame();
        });
        panel.add(stop);
    }

    private JPanel initVideo() {
        JPanel panel = new JPanel();
        setupSlider();
        // 367, 308
        panel.setSize(367, 308);
        panel.setLayout(new BorderLayout());
        panel.add(mVideoLabel, PAGE_START);
        mFrameLabel.setFont(font);
        panel.add(mFrameLabel, PAGE_END);
        panel.add(mSlider);
        mVideoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        mFrameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        mSlider.setAlignmentX(Component.CENTER_ALIGNMENT);

        MouseListener mouseListener = new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                log("mouserClicked: x=" + e.getX() + " y=" + e.getY());
                checkLink(e.getX(), e.getY());
            }
        };
        mVideoLabel.addMouseListener(mouseListener);

        return panel;
    }

    private void checkLink(int x, int y){
//        ArrayList<LinkedObj> frameLinks = mCurrentLinks.getLinks().get(mCurrentProgress);
//        if (frameLinks != null){
//            for (LinkedObj obj : frameLinks){
//                if (x >= obj.getX() && x <= obj.getX() + obj.getWidth() &&
//                        y >= obj.getY() && y <= obj.getY() + obj.getHeight()){
//                    navigateTo(obj.getFilePath(), obj.getFrameIndex());
//                }
//            }
//        }
    }

    private void setupSlider() {
        mSlider.addChangeListener(this);
        mSlider.setMinorTickSpacing(1);
        mSlider.setMajorTickSpacing(100);
        mSlider.setPaintTrack(true);
        mSlider.setBorder(BorderFactory.createEmptyBorder(0,0,10,0));
        mSlider.setFont(font);
    }

    /** Listen to the slider. */
    public void stateChanged(ChangeEvent e) {
        JSlider source = (JSlider)e.getSource();
        if (!source.getValueIsAdjusting()) {
            mCurrentProgress = source.getValue();
            updateFrame();
        }
    }

}
