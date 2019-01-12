package ui;

import com.lynden.gmapsfx.javascript.object.GoogleMap;
import com.lynden.gmapsfx.javascript.object.LatLong;
import com.lynden.gmapsfx.javascript.object.MVCArray;
import com.lynden.gmapsfx.shapes.Polygon;
import com.lynden.gmapsfx.shapes.PolygonOptions;

class Triangle extends Polygon {
    final double TIP_OFFSET = 0.0001125;
    final double SIDE_OFFSET = 0.0000625;

    // Main point of where the triangle will be based o
    private LatLong markerLatLong;
    private LatLong triangleACoords;
    private LatLong triangleBCoords;
    private LatLong triangleCCoords;
    private LatLong triangleDCoords;

    private MVCArray markerOverlayCoords;
    private GoogleMap map;

    private double triangleTipOffset;
    private double triangleSideOffset;

    public Triangle(LatLong markerLatLong, GoogleMap map) {
        this(markerLatLong, map ,"blue", "lightBlue", 2, 0.5);
    }

    public Triangle(LatLong markerLatLong, GoogleMap map, String color, String fillColor) {
        this(markerLatLong, map, color, fillColor, 2, 0.5);
    }

    public Triangle(LatLong markerLatLong, GoogleMap map, String color, String fillColor, int strokeWeight, double fillOpacity) {
        super(new PolygonOptions()
                .strokeColor(color)
                .strokeWeight(strokeWeight)
                .editable(false)
                .fillColor(fillColor)
                .fillOpacity(fillOpacity));

        this.map = map;
        calculateOffset();
        this.markerLatLong = markerLatLong;

        this.triangleACoords = new LatLong(markerLatLong.getLatitude(), markerLatLong.getLongitude() + triangleTipOffset);
        this.triangleBCoords = new LatLong(markerLatLong.getLatitude() - triangleSideOffset, markerLatLong.getLongitude() - triangleSideOffset);
        this.triangleCCoords = new LatLong(markerLatLong.getLatitude() + triangleSideOffset, markerLatLong.getLongitude() - triangleSideOffset);
        this.triangleDCoords = new LatLong(markerLatLong.getLatitude(), markerLatLong.getLongitude() + triangleTipOffset);
        this.markerOverlayCoords = new MVCArray(new LatLong[]{triangleACoords, triangleBCoords, triangleCCoords, triangleDCoords});

        setPaths(markerOverlayCoords);

        map.addMapShape(this);
    }

    private void calculateOffset() {
        // Determines the size of the triangles depending on the zoom of the map (Thanks stackoverflow)
        double metersPerPx = 156543.03392 * Math.cos(map.getCenter().getLatitude() * Math.PI / 180) / Math.pow(2, map.getZoom());
        this.triangleTipOffset = TIP_OFFSET * metersPerPx;
        this.triangleSideOffset = SIDE_OFFSET * metersPerPx;
    }

    public void refreshTriangle() {
        refreshTriangle(this.markerLatLong);
    }

    public void refreshTriangle(LatLong newMarker) {
        calculateOffset();
        // To determine the direction that the tip of the triangle points to we need to
        //  first find the angle that the triangle will be rotating then actually rotate it
        double x = newMarker.getLatitude() - markerLatLong.getLatitude(); // x & y are now relative to 0,0
        double y = newMarker.getLongitude() - markerLatLong.getLongitude();
        double angle = -1 * Math.atan2(x, y);
        double sin = Math.sin(angle);
        double cos = Math.cos(angle);

        /*
        System.out.println("Original markerLatLong: " + markerLatLong.getLatitude() + ", " + markerLatLong.getLongitude() + "new: " + newMarker.getLatitude() + ", " + newMarker.getLongitude());
        System.out.println("Angle: " + Math.toDegrees(angle));
        System.out.println("sin: " + sin);
        System.out.println("cos: " + cos);
        */
        // Equation for rotating points along the origin
        // x′=xcosθ−ysinθ
        // y′=ycosθ+xsinθ
        double aCoords[] = {(-1 * triangleTipOffset * sin), triangleTipOffset * cos};
        double bCoords[] = {(-1 * triangleSideOffset * cos) - (-1 * triangleSideOffset * sin), (-1 * triangleSideOffset * cos) + (-1 * triangleSideOffset * sin)};
        double cCoords[] = {triangleSideOffset * cos - (-1 * triangleSideOffset * sin), (-1 * triangleSideOffset * cos) + (triangleSideOffset * sin)};
        double dCoords[] = {(-1 * triangleTipOffset * sin), triangleTipOffset * cos};

        // Move the basis from the origin back to the location of the marker
        aCoords[0] += newMarker.getLatitude();
        bCoords[0] += newMarker.getLatitude();
        cCoords[0] += newMarker.getLatitude();
        dCoords[0] += newMarker.getLatitude();
        aCoords[1] += newMarker.getLongitude();
        bCoords[1] += newMarker.getLongitude();
        cCoords[1] += newMarker.getLongitude();
        dCoords[1] += newMarker.getLongitude();

        this.triangleACoords = new LatLong(aCoords[0], aCoords[1]);
        this.triangleBCoords = new LatLong(bCoords[0], bCoords[1]);
        this.triangleCCoords = new LatLong(cCoords[0], cCoords[1]);
        this.triangleDCoords = new LatLong(dCoords[0], dCoords[1]);
        this.markerOverlayCoords = new MVCArray(new LatLong[]{triangleACoords, triangleBCoords, triangleCCoords, triangleDCoords});

        setPath(markerOverlayCoords);
    }

    public Polygon getPolygon() {
        return this;
    }
}
