package org.roger600.lienzo.client;

import com.ait.lienzo.client.core.Attribute;
import com.ait.lienzo.client.core.event.*;
import com.ait.lienzo.client.core.shape.Layer;
import com.ait.lienzo.client.core.shape.MultiPath;
import com.ait.lienzo.client.core.shape.Rectangle;
import com.ait.lienzo.client.core.shape.wires.IControlHandle;
import com.ait.lienzo.client.core.shape.wires.IControlHandleList;
import com.ait.lienzo.client.core.types.BoundingBox;
import com.ait.lienzo.client.core.types.PathPartList;
import com.ait.lienzo.shared.core.types.ColorName;
import com.ait.tooling.common.api.flow.Flows;
import com.google.gwt.core.client.GWT;

import java.util.Map;

import static com.ait.lienzo.client.core.AttributeOp.any;

public class MultiPathAttributesChangedTests implements MyLienzoTest {

    private static final Flows.BooleanOp XY_OP  = any( Attribute.X, Attribute.Y );
    private static final Flows.BooleanOp WH_OP  = any( Attribute.WIDTH, Attribute.HEIGHT );
    private static final Flows.BooleanOp POINTS_OP  = any( Attribute.PATH );

    private final IAttributesChangedBatcher attributesChangedBatcher = new AnimationFrameAttributesChangedBatcher();
    private IControlHandleList m_ctrls;
    private MultiPath    m_multi;
    private Layer layer;

    public void test( final Layer layer ) {
        this.layer = layer;

        m_multi = new MultiPath()
                .rect( 100, 100, 100, 100 )
                .setFillColor( ColorName.RED )
                .setDraggable( true );

        applyPathResize();

        layer.add( m_multi );

        addButtons( 0, 0 );

        initAttChangedHandler();
   }

    private void initAttChangedHandler()
    {
        m_multi.setAttributesChangedBatcher(attributesChangedBatcher);

        final AttributesChangedHandler handler = new AttributesChangedHandler()
        {
            @Override
            public void onAttributesChanged(AttributesChangedEvent event)
            {
                GWT.log( "HANDLER FIRED -> " + event.toJSONString() );

                if (event.evaluate(XY_OP))
                {
                    GWT.log( "SHAPE MOVED TO [" + m_multi.getX() + ", "
                    + m_multi.getY() + "]!!");
                }
                if (event.evaluate(WH_OP))
                {
                    BoundingBox bb = m_multi.getBoundingBox();
                    GWT.log( "SHAPE RESIZED TO [" + bb.getWidth() + ", "
                            + bb.getHeight() + "]!!");
                }
                if (event.evaluate(POINTS_OP))
                {
                    BoundingBox bb = m_multi.getBoundingBox();
                    GWT.log( "SHAPE PATH RESIZED TO [" + bb.getWidth() + ", "
                            + bb.getHeight() + "]!!");
                }
            }
        };

        // Attribute change handlers.
        m_multi.addAttributesChangedHandler(Attribute.X, handler);
        m_multi.addAttributesChangedHandler(Attribute.Y, handler);
        m_multi.addAttributesChangedHandler(Attribute.WIDTH, handler);
        m_multi.addAttributesChangedHandler(Attribute.HEIGHT, handler);
        m_multi.addAttributesChangedHandler(Attribute.PATH, handler);

        Attribute[] allAttrs = getAllAttributes();
        for ( final Attribute a : allAttrs ) {
            m_multi.addAttributesChangedHandler(a, handler);
        }

    }

    private void addButtons( final double x, final double y ) {
        Rectangle moveButton = new Rectangle( 100, 50 )
                .setX( x )
                .setY( y )
                .setFillColor( ColorName.BLUE );

        moveButton.addNodeMouseClickHandler( new NodeMouseClickHandler() {
            @Override
            public void onNodeMouseClick( NodeMouseClickEvent event ) {

                m_multi.setX( 500 ).setY( 500 );
                //m_multi.setFillColor( ColorName.BLACK );
                layer.batch();
            }
        } );

        layer.add( moveButton );
    }

    private void applyPathResize() {
        m_multi.addNodeMouseClickHandler(new NodeMouseClickHandler()
        {
            @Override
            public void onNodeMouseClick(NodeMouseClickEvent event)
            {
                if (event.isShiftKeyDown())
                {
                    if (null != m_ctrls)
                    {
                        m_ctrls.destroy();

                        m_ctrls = null;
                    }
                    Map<IControlHandle.ControlHandleType, IControlHandleList> hmap = m_multi.getControlHandles( IControlHandle.ControlHandleStandardType.RESIZE);

                    if (null != hmap)
                    {
                        m_ctrls = hmap.get( IControlHandle.ControlHandleStandardType.RESIZE);

                        if ((null != m_ctrls) && (m_ctrls.isActive()))
                        {
                            m_ctrls.show();
                        }
                    }
                }
                else if (event.isAltKeyDown())
                {
                    if (null != m_ctrls)
                    {
                        m_ctrls.destroy();

                        m_ctrls = null;
                    }
                    Map<IControlHandle.ControlHandleType, IControlHandleList> hmap = m_multi.getControlHandles( IControlHandle.ControlHandleStandardType.POINT);

                    if (null != hmap)
                    {
                        m_ctrls = hmap.get( IControlHandle.ControlHandleStandardType.POINT);

                        if ((null != m_ctrls) && (m_ctrls.isActive()))
                        {
                            m_ctrls.show();
                        }
                    }
                }
            }
        });
    }


    private Attribute[] getAllAttributes() {
        return new Attribute[] {
                Attribute.HEIGHT,
                Attribute.CORNER_RADIUS,
                Attribute.FILL,
                Attribute.STROKE,
                Attribute.STROKE_WIDTH,
                Attribute.LINE_JOIN,
                Attribute.X,
                Attribute.Y,
                Attribute.VISIBLE,
                Attribute.LISTENING,
                Attribute.ID,
                Attribute.NAME,
                Attribute.ALPHA,
                Attribute.FILL_ALPHA,
                Attribute.STROKE_ALPHA,
                Attribute.SCALE,
                Attribute.ROTATION,
                Attribute.OFFSET,
                Attribute.SHEAR,
                Attribute.TRANSFORM,
                Attribute.DRAGGABLE,
                Attribute.EDITABLE,
                Attribute.FILL_SHAPE_FOR_SELECTION,
                Attribute.FILL_BOUNDS_FOR_SELECTION,
                Attribute.DRAG_CONSTRAINT,
                Attribute.DRAG_BOUNDS,
                Attribute.RADIUS,
                Attribute.RADIUS_X,
                Attribute.RADIUS_Y,
                Attribute.CLEAR_LAYER_BEFORE_DRAW,
                Attribute.TRANSFORMABLE,
                Attribute.TEXT,
                Attribute.FONT_SIZE,
                Attribute.FONT_FAMILY,
                Attribute.FONT_STYLE,
                Attribute.POINTS,
                Attribute.STAR_POINTS,
                Attribute.LINE_CAP,
                Attribute.DASH_ARRAY,
                Attribute.DASH_OFFSET,
                Attribute.SIDES,
                Attribute.OUTER_RADIUS,
                Attribute.INNER_RADIUS,
                Attribute.SKEW,
                Attribute.SHADOW,
                Attribute.START_ANGLE,
                Attribute.END_ANGLE,
                Attribute.COUNTER_CLOCKWISE,
                Attribute.CONTROL_POINTS,
                Attribute.TEXT_BASELINE,
                Attribute.TEXT_ALIGN,
                Attribute.TEXT_UNIT,
                Attribute.CLIPPED_IMAGE_WIDTH,
                Attribute.CLIPPED_IMAGE_HEIGHT,
                Attribute.CLIPPED_IMAGE_START_X,
                Attribute.CLIPPED_IMAGE_START_Y,
                Attribute.CLIPPED_IMAGE_DESTINATION_WIDTH,
                Attribute.CLIPPED_IMAGE_DESTINATION_HEIGHT,
                Attribute.SERIALIZATION_MODE,
                Attribute.URL,
                Attribute.LOOP,
                Attribute.AUTO_PLAY,
                Attribute.PLAYBACK_RATE,
                Attribute.SHOW_POSTER,
                Attribute.VOLUME,
                Attribute.BASE_WIDTH,
                Attribute.HEAD_WIDTH,
                Attribute.ARROW_ANGLE,
                Attribute.BASE_ANGLE,
                Attribute.ARROW_TYPE,
                Attribute.MITER_LIMIT,
                Attribute.CURVE_FACTOR,
                Attribute.ANGLE_FACTOR,
                Attribute.LINE_FLATTEN,
                Attribute.TOP_WIDTH,
                Attribute.BOTTOM_WIDTH,
                Attribute.IMAGE_SELECTION_MODE,
                Attribute.DRAG_MODE,
                Attribute.PATH,
                Attribute.TICK_RATE,
                Attribute.SPRITE_BEHAVIOR_MAP,
                Attribute.SPRITE_BEHAVIOR,
                Attribute.ACTIVE,
                Attribute.VALUE,
                Attribute.COLOR,
                Attribute.MATRIX,
                Attribute.INVERTED,
                Attribute.GAIN,
                Attribute.BIAS,
                Attribute.HEAD_OFFSET,
                Attribute.TAIL_OFFSET,
                Attribute.CORRECTION_OFFSET,
                Attribute.HEAD_DIRECTION,
                Attribute.TAIL_DIRECTION,
                Attribute.EVENT_PROPAGATION_MODE
        };
    }
}
