package com.player.ui;

import com.sun.istack.internal.Nullable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;

import static com.player.ui.Utils.log;
import static com.player.ui.Utils.readImage;
import static com.player.ui.Utils.selectFile;

public class Player {

    @Nullable
    private File primaryDir;

    @Nullable
    private File secondaryDir;

    private JFrame mJFrame = new JFrame();
    private JLabel mLeftVideo = new JLabel();
    private JLabel mRightVideo = new JLabel();

    public static void main(String[] args) {
        new Player();
        log("Oki");
    }

    private Player() {
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
        loadFrame(0, 1);
    }

    private void importSecondaryVideo() {
        secondaryDir = selectFile(mJFrame);
        loadFrame(1, 1);
    }

    /**
     *
     * @param type primaryVideo 0, secondaryVideo 1
     * @param frameNumber from 1 - 9000
     */
    private void loadFrame(int type, int frameNumber) {
        File dir = null;
        if (type == 0) {
            dir = primaryDir;
        } else if (type == 1) {
            dir = secondaryDir;
        }
        if (dir == null) {
            return;
        }
        String fileName = dir.getName() + String.format("%04d", frameNumber) + ".rgb";
        log(fileName);
        String filePath = dir.getAbsolutePath() + File.separator + fileName;
        log(filePath);
        BufferedImage image = readImage(filePath);
        if (type == 0) {
            mLeftVideo.setIcon(new ImageIcon(image));
        } else if (type == 1) {
            mRightVideo.setIcon(new ImageIcon(image));
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
