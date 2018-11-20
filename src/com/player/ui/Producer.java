package com.player.ui;

import com.sun.istack.internal.Nullable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;

import static com.player.ui.Utils.*;

public class Producer {

    @Nullable
    private File primaryDir;

    @Nullable
    private File secondaryDir;

    private JFrame mJFrame = new JFrame();

    private JLabel mLeftVideo = new JLabel();
    private int primaryProgress = 0;

    private JLabel mRightVideo = new JLabel();
    private int secondaryProgress = 0;

    private final int mDelay = 33;

    private ActionListener primaryUpdater = evt -> {
        mLeftVideo.setIcon(new ImageIcon(loadFrame(primaryDir, primaryProgress)));
        primaryProgress++;
        if (primaryProgress > 9000) {
            primaryProgress = 1;
        }
    };

    private ActionListener secondaryUpdater = evt -> {
        mRightVideo.setIcon(new ImageIcon(loadFrame(secondaryDir, secondaryProgress)));
        secondaryProgress++;
        if (secondaryProgress > 9000) {
            secondaryProgress = 1;
        }
    };

    private Timer primaryTimer = new Timer(mDelay, primaryUpdater);
    private Timer secondaryTimer = new Timer(mDelay, secondaryUpdater);

    public static void main(String[] args) {
        new Producer();
        log("Oki");
    }

    private Producer() {
        initUi();
    }

    private void initUi() {
        initJFrame();

        JPanel actionPanel = initActionPanel();
        JPanel leftVideo = initVideo(0);
        JPanel rightVideo = initVideo(1);

//        JButton vegFruitBut = new JButton( "Fruit or Veg");
//        vegFruitBut.addActionListener(event -> {
//            actionPanel.setVisible(!actionPanel.isVisible());
//            comboPanel.setVisible(!comboPanel.isVisible());
//        });

        mJFrame.add(actionPanel, BorderLayout.PAGE_START);
        mJFrame.add(leftVideo, BorderLayout.LINE_START);
        mJFrame.add(rightVideo, BorderLayout.LINE_END);
        mJFrame.setVisible(true);
    }

    private void initJFrame() {
        mJFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        mJFrame.setTitle("Oki Player");
        mJFrame.setSize(734,408);
        mJFrame.setLocationRelativeTo(null);
    }

    private JPanel initActionPanel() {
        JPanel panel = new JPanel();
        panel.setSize(734, 100);
        FlowLayout layout = new FlowLayout();
        panel.setLayout(layout);

        setupActionList(panel);

        setupLinkList(panel);

        setupButtons(panel);

        return panel;
    }

    private void setupActionList(JPanel panel) {
        JLabel actionLabel = new JLabel("Action:");
        String[] actionOptions = {
                "Import Primary Video",
                "Import Secondary Video",
                "Create new hyperlink"
        };
        JList<String> actionList = new JList<>(actionOptions);
        actionList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        actionList.setLayoutOrientation(JList.VERTICAL);

        MouseListener mouseListener = new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 1) {
                    int index = actionList.getSelectedIndex();
                    if (index == 0){
                        importPrimaryVideo();
                    } else if (index == 1) {
                        importSecondaryVideo();
                    } else if (index == 2) {
                        createNewLink();
                    }
                }
            }
        };
        actionList.addMouseListener(mouseListener);

        panel.add(actionLabel);
        panel.add(actionList);
    }

    private void importPrimaryVideo() {
        primaryDir = selectFile(mJFrame);
        primaryProgress = 1;
        startPlay(0);
    }

    private void importSecondaryVideo() {
        secondaryDir = selectFile(mJFrame);
        secondaryProgress = 1;
        startPlay(1);
    }

    /**
     *
     * @param frameNumber from 1 - 9000
     */
    private BufferedImage loadFrame(File dir, int frameNumber) {
        String fileName = dir.getName() + String.format("%04d", frameNumber) + ".rgb";
        String filePath = dir.getAbsolutePath() + File.separator + fileName;
        return readImage(filePath);
    }

    private void startPlay(int type) {
        int delay = 33; //milliseconds
        if (type == 0) {
            primaryTimer.start();
        } else if (type == 1) {
            secondaryTimer.start();
        }
    }

    private void createNewLink() {

    }

    private void setupLinkList(JPanel panel) {
        JLabel linkLabel = new JLabel("Select Link:");
        String[] links = {
                "PlaceHolder1",
                "PlaceHolder2"
        };
        JList<String> linkList = new JList<>(links);
        linkList.setLayoutOrientation(JList.VERTICAL);
        panel.add(linkLabel);
        panel.add(linkList);
    }

    private void setupButtons(JPanel panel) {
        JButton connect_video = new JButton("Connect Video");
        panel.add(connect_video);

        JButton save_file = new JButton("Save File");
        panel.add(save_file);
    }

    private JPanel initVideo(int type) {
        JPanel panel = new JPanel();
        panel.setSize(367, 308);

        if (type == 0) {
            panel.add(mLeftVideo);
        } else if (type == 1) {
            panel.add(mRightVideo);
        }

        return panel;
    }

}
