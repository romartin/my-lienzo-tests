package org.roger600.lienzo.client;

import com.ait.lienzo.client.core.image.PictureLoadedHandler;
import com.ait.lienzo.client.core.shape.Layer;
import com.ait.lienzo.client.core.shape.Picture;
import com.ait.lienzo.client.core.types.BoundingBox;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.regexp.shared.MatchResult;
import com.google.gwt.regexp.shared.RegExp;
import com.google.gwt.resources.client.DataResource;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.Widget;
import org.roger600.lienzo.client.resources.LienzoTestsResources;
import org.roger600.lienzo.client.util.Base64Util;

public class SVGPicturesTests implements MyLienzoTest,
                                         HasButtons {

    public static final String SVG_DATA_URI_B64 = "data:image/svg+xml;base64,";
    public static final String SVG_DATA_URI_XML = "data:image/svg+xml;utf8,";
    private static final double INC_W = 25;
    private static final double INC_H = 25;
    DataResource taskComposite = LienzoTestsResources.INSTANCE.taskComposite();
    DataResource taskUserComposite = LienzoTestsResources.INSTANCE.taskUserComposite();
    DataResource taskScriptComposite = LienzoTestsResources.INSTANCE.taskScriptComposite();
    String hrefPattern = ".*xlink:href=\"(.*\\.svg).*";

    private double w = 448;
    private double h = 448;
    private Layer layer;
    private Picture picture;
    private Button scaleButton;

    @Override
    public void test(final Layer layer) {
        this.layer = layer;
        new Picture(LienzoTestsResources.INSTANCE.edit().getSafeUri().asString(),
                    new PictureLoadedHandler() {
                        @Override
                        public void onPictureLoaded(Picture picture) {
                            SVGPicturesTests.this.picture = picture;
                            picture.setX(105)
                                    .setY(105);
                            scalePicture(picture, 15, 15);
                            layer.add(picture);
                        }
                    });
        new Picture(LienzoTestsResources.INSTANCE.delete().getSafeUri().asString(),
                    new PictureLoadedHandler() {
                        @Override
                        public void onPictureLoaded(Picture picture) {
                            SVGPicturesTests.this.picture = picture;
                            picture.setX(305)
                                    .setY(105);
                            scalePicture(picture, 15, 15);
                            layer.add(picture);
                        }
                    });
        new Picture(LienzoTestsResources.INSTANCE.gears().getSafeUri().asString(),
                    new PictureLoadedHandler() {
                        @Override
                        public void onPictureLoaded(Picture picture) {
                            SVGPicturesTests.this.picture = picture;
                            picture.setX(505)
                                    .setY(105);
                            scalePicture(picture, 15, 15);
                            layer.add(picture);
                        }
                    });
    }

    @Override
    public void setButtonsPanel(Panel panel) {

        // Some buttons for test scaling.
        scaleButton = new Button("(" + w + ", " + h + ")");
        scaleButton.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent clickEvent) {
                scalePicture(w,
                             h);
            }
        });
        panel.add(scaleButton);

        final Button b1 = new Button("(+)");
        b1.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent clickEvent) {
                SVGPicturesTests.this.w = w + INC_W;
                SVGPicturesTests.this.h = h + INC_H;
                scalePicture(w,
                             h);
            }
        });
        panel.add(b1);

        final Button b2 = new Button("(-)");
        b2.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent clickEvent) {
                SVGPicturesTests.this.w = w - INC_W;
                SVGPicturesTests.this.h = h - INC_H;
                scalePicture(w,
                             h);
            }
        });
        panel.add(b2);

        // Append the SVG into the DOM.
        Widget svgCompositeImage = getSvgCompositeImage();
        panel.add(svgCompositeImage);
    }

    private Widget getSvgCompositeImage() {
        HorizontalPanel main = new HorizontalPanel();
        FlowPanel imagePanel1 = new FlowPanel();
        String content = getSvgCompositeContent(true,
                                                taskComposite,
                                                taskUserComposite,
                                                taskScriptComposite);
        imagePanel1.getElement().setInnerHTML(content);
        main.add(imagePanel1);
        return main;
    }

    private void scalePicture(final double width,
                              final double height) {
        scalePicture(picture,
                     w,
                     h);
        scaleButton.setText("(" + w + ", " + h + ")");
        layer.draw();
    }

    private String getSvgCompositeContent(boolean replaceHrefs,
                                          DataResource... resources) {
        String mainContent = getSVGContent(resources[0]);
        if (replaceHrefs) {
            mainContent = replaceHref(mainContent);
        }
        mainContent = mainContent.replaceAll("<\\?xml version=\"1\\.0\" encoding=\"utf-8\"\\?>",
                                             "");
        if (resources.length > 1) {
            String childContent = "";
            for (int i = 1; i < resources.length; i++) {
                DataResource resource = resources[i];
                String content = getSVGContent(resource);
                if (replaceHrefs) {
                    content = replaceHref(content);
                }
                content = content.replaceAll("<\\?xml version=\"1\\.0\" encoding=\"utf-8\"\\?>",
                                             "");
                childContent += content;
            }

            return "<svg  version=\"1.1\" xmlns=\"http://www.w3.org/2000/svg\" xmlns:xlink=\"http://www.w3.org/1999/xlink\" " +
                    "width=\"448px\" height=\"448px\" viewBox=\"0 0 448 448\">\n" +
                    mainContent +
                    "<defs>"
                    + childContent
                    + "</defs>"
                    + "</svg>";
        }
        return mainContent;
    }

    public String getSVGContent(DataResource resource) {
        String dataUri = resource.getSafeUri().asString();

        if (dataUri.startsWith(SVG_DATA_URI_B64)) {
            try {
                String content = dataUri.substring(SVG_DATA_URI_B64.length());

                return new String(Base64Util.decode(content));
            } catch (Exception ex) {
                GWT.log("[SVGIconRenderer] Impossible to get SVG content for '" + dataUri + "'");
                GWT.log("[SVGIconRenderer] Error: " + ex.getMessage());
            }
        }

        return null;
    }

    private String replaceHref(String text) {
        RegExp thePattern = RegExp.compile(hrefPattern);
        String result = text;
        while (thePattern.test(result)) {
            MatchResult matchResult = thePattern.exec(result);
            if (matchResult != null && matchResult.getGroupCount() == 2) {
                GWT.log("MATCH");
                String toReplace = matchResult.getGroup(1);
                result = result.replaceAll(toReplace,
                                           "");
            } else {
                GWT.log("NO MATCH");
            }
        }
        return result;
    }

    private static void scalePicture(final Picture picture,
                                     final double width,
                                     final double height) {
        final BoundingBox bb = picture.getBoundingBox();
        final double[] scale = getScaleFactor(bb.getWidth(),
                                              bb.getHeight(),
                                              width,
                                              height);
        picture.setScale(scale[0],
                         scale[1]);
    }

    private static double[] getScaleFactor(final double width,
                                           final double height,
                                           final double targetWidth,
                                           final double targetHeight) {
        return new double[]{
                width > 0 ? targetWidth / width : 1,
                height > 0 ? targetHeight / height : 1};
    }

    @Override
    public int compareTo(MyLienzoTest other) {
        return this.getClass().getSimpleName().compareTo(other.getClass().getSimpleName());
    }
}
