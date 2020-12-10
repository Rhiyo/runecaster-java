package com.game.ECS.Tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.Map;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.CircleMapObject;
import com.badlogic.gdx.maps.objects.EllipseMapObject;
import com.badlogic.gdx.maps.objects.PolygonMapObject;
import com.badlogic.gdx.maps.objects.PolylineMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.objects.TextureMapObject;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Ellipse;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.game.ECS.Storage.B2DVars;
import com.game.ECS.Storage.GameVars;

import java.math.BigDecimal;

/**
 * Created by Sean on 12/04/2015.
 *
 * Gets collision objects from Tiled map and turns them in Box2d objects
 *
 * Has to convert the Tiled world objects to the Games metres. then convert the games metres to
 * the Box2D metres
 *
 * Stolen from: http://gamedev.stackexchange.com/questions/66924/how-can-i-convert-a-tilemap-to-a-box2d-world
 *
 */
public class MapBodyBuilder {

    // The pixels per tile. If your tiles are 16x16, this is set to 16f
    private static float ppt = 0;
    private static World pWorld = null;

    public static Array<Body> buildShapes(Map map, String layer, World world) {
        ppt = GameVars.PTM;
        pWorld = world;
        MapObjects objects = map.getLayers().get(layer).getObjects();

        Array<Body> bodies = new Array<Body>();

        for(MapObject object : objects) {

            if (object instanceof TextureMapObject) {
                continue;
            }

            Shape shape = null;
            Body body = null;
            if (object instanceof RectangleMapObject) {
                shape = getRectangle((RectangleMapObject)object);
            }
            else if (object instanceof PolygonMapObject) {
                shape = getPolygon((PolygonMapObject)object);
            }
            else if (object instanceof PolylineMapObject) {
                shape = getPolyline((PolylineMapObject)object);
            }
            else if (object instanceof CircleMapObject) {
                shape = getCircle((CircleMapObject)object);
            }else if (object instanceof EllipseMapObject) {
                Ellipse ellipse = ((EllipseMapObject) object).getEllipse();
                if(ellipse.width == ellipse.height){
                    CircleShape circleShape = new CircleShape();
                    circleShape.setRadius((ellipse.width /2 / ppt) /ppt);
                    circleShape.setPosition(
                            new Vector2(((ellipse.x + (ellipse.width /2)) / ppt)/ppt,
                                    ((ellipse.y + (ellipse.height /2)) / ppt )/ppt));
                    shape = circleShape;
                }else {
                    //body = getEllipses((EllipseMapObject) object);
                }
            }
            else {
                continue;
            }

            if(shape != null) {
                FixtureDef fdef = new FixtureDef();
                fdef.shape = shape;
                fdef.density = 1;
                BodyDef bd = new BodyDef();
                bd.type = BodyDef.BodyType.StaticBody;
                body = pWorld.createBody(bd);
                if(layer.equals("Hitbox")) {
                    fdef.filter.categoryBits = B2DVars.BIT_HITBOX;
                    fdef.filter.maskBits = B2DVars.BIT_PROJECTILE;
                }
                if(layer.equals("Collision")) {
                    fdef.filter.categoryBits = B2DVars.BIT_COLLISION;
                    fdef.filter.maskBits = B2DVars.BIT_HUSK;
                }
                Fixture fixture = body.createFixture(fdef);
                //TODO reorganize this
                if(layer.equals("Hitbox")) {
                    fixture.setUserData("hitbox");
                }
                if(layer.equals("Collision")) {
                    fixture.setUserData("collision");
                }
                shape.dispose();
                bodies.add(body);
            }



        }
        return bodies;
    }

    private static PolygonShape getRectangle(RectangleMapObject rectangleObject) {
        Rectangle rectangle = rectangleObject.getRectangle();
        PolygonShape polygon = new PolygonShape();
        Vector2 size = new Vector2(((rectangle.x + rectangle.width * 0.5f) / ppt)/ppt,
                ((rectangle.y + rectangle.height * 0.5f ) / ppt)/ppt);
        polygon.setAsBox((rectangle.width * 0.5f / ppt)/ppt,
                (rectangle.height * 0.5f / ppt)/ppt,
                size,
                0.0f);
        return polygon;
    }

    private static CircleShape getCircle(CircleMapObject circleObject) {
        Circle circle = circleObject.getCircle();
        CircleShape circleShape = new CircleShape();
        circleShape.setRadius((circle.radius / ppt) /ppt);
        circleShape.setPosition(new Vector2((circle.x / ppt)/ppt, (circle.y / ppt )/ppt));
        return circleShape;
    }

    private static PolygonShape getPolygon(PolygonMapObject polygonObject) {
        PolygonShape polygon = new PolygonShape();
        float[] vertices = polygonObject.getPolygon().getTransformedVertices();

        float[] worldVertices = new float[vertices.length];

        for (int i = 0; i < vertices.length; ++i) {
            //System.out.println(vertices[i]);
            worldVertices[i] = (vertices[i] / ppt)/ppt;
        }

        polygon.set(worldVertices);
        return polygon;
    }

    private static ChainShape getPolyline(PolylineMapObject polylineObject) {
        float[] vertices = polylineObject.getPolyline().getTransformedVertices();
        Vector2[] worldVertices = new Vector2[vertices.length / 2];

        for (int i = 0; i < vertices.length / 2; ++i) {
            worldVertices[i] = new Vector2();
            worldVertices[i].x = vertices[i * 2] / ppt;
            worldVertices[i].y = vertices[i * 2 + 1] / ppt;
        }

        ChainShape chain = new ChainShape();
        chain.createChain(worldVertices);
        return chain;
    }

    private static Body getEllipses(EllipseMapObject ellipseObject) {

        Ellipse ellipse = ellipseObject.getEllipse();
        Gdx.app.log("T", "E");
        double a = (5), b = (2);
        int n = 8; //Scale this depening on width and height
        double[] x = new double[n];
        double[] y = new double[n];

        for(int i = 0; i < n; i++) {
            double theta = Math.PI / 2 * i / n;
            double fi = Math.PI / 2 - Math.atan(Math.tan(theta) * a / b);
            x[i] = a * Math.cos(fi);
            y[i] = b * Math.sin(fi);
        }

        Vector2[] verticestr = new Vector2[n];
        for (int i = 0; i < n; i++) {
            //Gdx.app.log("YA", x[i] + " " + y[i]);
            verticestr[i] = new Vector2(round((((float) x[i])/32)/32,4), round((((float) y[i])/32)/32,4));
            Gdx.app.log("HA", verticestr[i].x + " " + verticestr[i].y);
        }

        /*/BR
        Vector2[] verticesbr = new Vector2[n];
        for (int i = 0; i < n - 1; i++) {
            verticesbr[i] = new Vector2(((float) x[i])/32/32, ((float) (y[i] - y[i] * 2))/32/32);
        }
        verticesbr[n - 1] = new Vector2(((float) x[n - 1])/32/32, ((float) (y[n - 1]))/32/32);

        //BL
        Vector2[] verticesbl = new Vector2[n];
        for (int i = 0; i < n; i++) {
            verticesbl[i] = new Vector2(((float) (x[i] - x[i] * 2))/32/32, ((float) (y[i] - y[i] * 2))/32/32);
        }

        //TL
        Vector2[] verticestl = new Vector2[n];
        verticestl[0] = new Vector2(((float) x[0])/32/32, ((float) (y[0]))/32/32);
        for (int i = 1; i < n; i++) {
            verticestl[i] = new Vector2(((float) (x[i] - x[i] * 2))/32/32, ((float) y[i])/32/32);
        }
        */

        BodyDef bdef = new BodyDef();
        bdef.position.set(ellipse.x/32/32, (float)(ellipse.y/32/32));
        bdef.type = BodyDef.BodyType.StaticBody;
        Body body = pWorld.createBody(bdef);

        PolygonShape polygon = new PolygonShape();

        polygon.set(verticestr);
        FixtureDef fdef = new FixtureDef();
        fdef.shape = polygon;
        fdef.density = 1;
        body.createFixture(fdef);

        /*/BR
        polygon = new PolygonShape();

        polygon.set(verticesbr);
        fdef = new FixtureDef();
        fdef.shape = polygon;
        body.createFixture(fdef);

        //BL
        polygon = new PolygonShape();

        polygon.set(verticesbl);
        fdef = new FixtureDef();
        fdef.shape = polygon;
        body.createFixture(fdef);

        //TL
        polygon = new PolygonShape();

        polygon.set(verticestl);
        fdef = new FixtureDef();
        fdef.shape = polygon;
        body.createFixture(fdef);
*/
        return body;
    }

    public static float round(float d, int decimalPlace) {
        BigDecimal bd = new BigDecimal(Float.toString(d));
        bd = bd.setScale(decimalPlace, BigDecimal.ROUND_HALF_UP);
        return bd.floatValue();
    }
}