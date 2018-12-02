package com.player.ui;

import com.google.gson.Gson;
import com.player.algo.CVObjTracker;
import com.player.entity.Frame;
import com.player.entity.ProducerLink;
import org.opencv.core.Rect2d;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.player.ui.Utils.*;
import static java.awt.BorderLayout.PAGE_END;
import static java.awt.BorderLayout.PAGE_START;
import static java.awt.Cursor.CROSSHAIR_CURSOR;

public class Producer {
    private static final int PRIMARY_VIDEO = 0;
    private static final int SECONDARY_VIDEO = 1;

    private File primaryDir;
    private File secondaryDir;

    private Map<Integer, Frame> mFrameMap = new HashMap<>();
    private List<ProducerLink> mProducerLinks = new ArrayList<>();

    private JFrame mJFrame = new JFrame();

    private DefaultListModel<ProducerLink> mListModel = new DefaultListModel<>();
    private JList<ProducerLink> mLinkList = new JList<>(mListModel);

    /**
     * primary panel
     */
    private JLabel mPrimaryImage = new JLabel();
    private BufferedImage mPrimaryBufferedImage;
    private JLabel mPrimaryFrameNumber = new JLabel();
    private int mPrimaryProgress = 0;

    /**
     * secondary panel
     */
    private JLabel mSecondaryImage = new JLabel();
    private BufferedImage mSecondaryBufferedImage;
    private JLabel mSecondaryFrameNumber = new JLabel();
    private int mSecondaryProgress = 0;

    public static void main(String[] args) {
        new Producer();
        log("Oki-Producer");
    }

    private Producer() {
        initProducer();
    }

    private void initProducer() {
        initFrame();

        JPanel actionPanel = initActionPanel();
        JPanel primaryPanel = initVideo(PRIMARY_VIDEO);
        JPanel secondaryPanel = initVideo(SECONDARY_VIDEO);

        mJFrame.add(actionPanel, PAGE_START);
        mJFrame.add(primaryPanel, BorderLayout.LINE_START);
        mJFrame.add(secondaryPanel, BorderLayout.LINE_END);
        mJFrame.setVisible(true);
    }

    private void initFrame() {
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
        if (primaryDir != null) {
            updatePrimaryImage(1);
            mFrameMap = new HashMap<>();
        }
    }

    private void importSecondaryVideo() {
        secondaryDir = selectFile(mJFrame);
        if (secondaryDir != null) {
            updateSecondaryImage(1);
        }
    }

    private void updatePrimaryImage(int frameNum) {
        BufferedImage bufferedImage = loadFrame(primaryDir, frameNum);
        mPrimaryBufferedImage = bufferedImage;
        if (bufferedImage == null) {
            log("Error: frame is null.");
        } else {
            drawBox(frameNum, bufferedImage);
            mPrimaryImage.setIcon(new ImageIcon(bufferedImage));
            mPrimaryFrameNumber.setText("Current Frame " + frameNum);
            mPrimaryProgress = frameNum;
        }
    }

    private void drawBox(int frameNum, BufferedImage image) {
        Frame frame = mFrameMap.get(frameNum);
        if (frame != null && !frame.getLinks().isEmpty()) {
            Graphics2D g2d = image.createGraphics();
            for (Frame.Link link: frame.getLinks()) {
                g2d.setColor(Color.GREEN);
                g2d.drawRect(link.getX(), link.getY(), link.getWidth(), link.getHeight());
            }
            g2d.dispose();
        }
    }

    private void updateSecondaryImage(int frameNum) {
        BufferedImage bufferedImage = loadFrame(secondaryDir, frameNum);
        mSecondaryBufferedImage = bufferedImage;
        if (bufferedImage == null) {
            log("Error: frame is null.");
        } else {
            mSecondaryImage.setIcon(new ImageIcon(bufferedImage));
            mSecondaryFrameNumber.setText("Current Frame " + frameNum);
            mSecondaryProgress = frameNum;
        }
    }

    private void createNewLink() {
        addMouseListener();
    }

    private void setupLinkList(JPanel panel) {
        JLabel linkLabel = new JLabel("Select Link:");
        mLinkList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        mLinkList.setLayoutOrientation(JList.VERTICAL);

        MouseListener mouseListener = new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                ProducerLink producerLink = mListModel.get(mLinkList.getSelectedIndex());
                updatePrimaryImage(producerLink.getStartFrame());
            }
        };
        mLinkList.addMouseListener(mouseListener);

        panel.add(linkLabel);
        panel.add(mLinkList);
    }

    private void setupButtons(JPanel panel) {
        JButton btnConnectVideo = new JButton("Connect Video");
        btnConnectVideo.addActionListener(e -> connectVideo());
        panel.add(btnConnectVideo);

        JButton btnSaveFile = new JButton("Save File");
        btnSaveFile.addActionListener(e -> saveFile());
        panel.add(btnSaveFile);
    }

    private void connectVideo() {
        int selectedIndex = mLinkList.getSelectedIndex();
        if (selectedIndex == -1) {
            return;
        }
        ProducerLink producerLink = mListModel.get(selectedIndex);

        if (secondaryDir == null) {
            // TODO popup error dialog
            return;
        }

        producerLink.setDestFrame(mSecondaryProgress);
        producerLink.setDestPath(secondaryDir.getAbsolutePath());
    }

    private void saveFile() {
        Map<Integer, Frame> frameMap = new HashMap<>();
        if (mListModel.size() > 0) {
            for (int i = 0; i < mListModel.size(); i++) {
                ProducerLink producerLink = mListModel.get(i);
                for (ProducerLink.BBox bbox: producerLink.getbBoxes()) {
                    int frameNum = bbox.getFrame();
                    Frame frame = frameMap.get(frameNum);
                    if (frame == null) {
                        frameMap.put(frameNum, new Frame(frameNum, bbox, producerLink.getDestFrame(), producerLink.getDestPath()));
                    } else {
                        frame.addLink(bbox, producerLink.getDestFrame(), producerLink.getDestPath());
                    }
                }
            }
        }
        String json = new Gson().toJson(frameMap.values());
        log(json);

        if (primaryDir == null || !primaryDir.exists()) {
            // TODO popup error dialog
            log("Error: primary dir is null");
            return;
        }
        String filePath = primaryDir.getAbsolutePath() + File.separator + primaryDir.getName() + ".json";
        try (Writer writer = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream(filePath), "utf-8"))) {
            writer.write(json);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // TODO improve layout setting
    private JPanel initVideo(int type) {
        JPanel panel = new JPanel();
        JSlider jSlider = setupSlider(type);
        panel.setSize(367, 308);
        panel.setLayout(new BorderLayout());
        if (type == 0) {
            panel.add(mPrimaryImage, PAGE_START);
            panel.add(mPrimaryFrameNumber, PAGE_END);
            panel.add(jSlider);
        } else if (type == 1) {
            panel.add(mSecondaryImage, PAGE_START);
            panel.add(mSecondaryFrameNumber, PAGE_END);
            panel.add(jSlider);
        }
        return panel;
    }

    private JSlider setupSlider(int type) {
        JSlider jSlider = new JSlider(JSlider.HORIZONTAL, 1, 9000, 1);
        jSlider.addChangeListener(e ->{
            JSlider source = (JSlider)e.getSource();
            if (!source.getValueIsAdjusting()) {
                if (type == PRIMARY_VIDEO) {
                    updatePrimaryImage(source.getValue());
                } else {
                    updateSecondaryImage(source.getValue());
                }
            }
        });
        jSlider.setMinorTickSpacing(1);
        jSlider.setMajorTickSpacing(100);
        jSlider.setPaintTrack(true);
        jSlider.setBorder(BorderFactory.createEmptyBorder(0,0,10,0));
        return jSlider;
    }

    private int x;
    private int y;
    private int width;
    private int height;
    private int linkId = 0;

    private MouseListener mouseListener = new MouseListener() {
        @Override
        public void mouseClicked(MouseEvent e) {
        }

        @Override
        public void mousePressed(MouseEvent e) {
            x = e.getX();
            y = e.getY();
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            width = e.getX() - x;
            height = e.getY() - y;
            log("mouserReleased: width=" + width + " height=" + height);
            updatePrimaryImage(e.getX(), e.getY(), false);
            mPrimaryImage.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            mPrimaryImage.removeMouseListener(this);
            mPrimaryImage.removeMouseMotionListener(mouseMotionListener);
            createLink();
        }

        @Override
        public void mouseEntered(MouseEvent e) {
            mPrimaryImage.setCursor(new Cursor(CROSSHAIR_CURSOR));
        }

        @Override
        public void mouseExited(MouseEvent e) {
        }
    };

    private MouseMotionListener mouseMotionListener = new MouseMotionListener() {
        @Override
        public void mouseDragged(MouseEvent e) {
            int currentX = e.getX();
            int currentY = e.getY();
            updatePrimaryImage(currentX, currentY, true);
        }

        @Override
        public void mouseMoved(MouseEvent e) {

        }
    };

    private void addMouseListener() {
        mPrimaryImage.addMouseListener(mouseListener);
        mPrimaryImage.addMouseMotionListener(mouseMotionListener);
    }

    private void createLink() {
        try {
            List<ProducerLink.BBox> bBoxes = CVObjTracker.trackObj(primaryDir,
                    mPrimaryProgress, new Rect2d(x, y, width, height), 500);
            ProducerLink producerLink = new ProducerLink(linkId++, "Default Link", mPrimaryProgress, bBoxes);
            String json = new Gson().toJson(producerLink);
            log(json);
            addLink(producerLink);
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    private void addLink(ProducerLink producerLink) {
        mProducerLinks.add(producerLink);
        mListModel.addElement(producerLink);
        for (ProducerLink.BBox bbox: producerLink.getbBoxes()) {
            int frameNum = bbox.getFrame();
            Frame frame = mFrameMap.get(frameNum);
            if (frame == null) {
                mFrameMap.put(frameNum, new Frame(frameNum, bbox));
            } else {
                frame.addBbox(bbox);
            }
        }
    }

    private void updatePrimaryImage(int currentX, int currentY, boolean temporal) {
        BufferedImage image;
        if (temporal) {
            image = deepCopy(mPrimaryBufferedImage);
        } else {
            image = mPrimaryBufferedImage;
        }
        Graphics2D g2d = image.createGraphics();
        g2d.setColor(Color.GREEN);
        g2d.drawRect(x, y, currentX - x, currentY - y);
        g2d.dispose();
        mPrimaryImage.setIcon(new ImageIcon(image));
    }

}
