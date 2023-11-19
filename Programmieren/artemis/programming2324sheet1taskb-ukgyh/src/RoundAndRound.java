public class RoundAndRound {
    private static final double CIRCLE1_RADIUS = 5.1;
    private static final double CIRCLE2_RADIUS = 17.5;
    private static final double CONE_HEIGHT = 10.3;
    private static final double ROUNDING_FACTOR = 1000.0;
    private static final double VOLUME_CALCULATION_FACTOR = 1 / 3.0;
    public static void main(String[] args) {
        Circle circle1 = new Circle();
        circle1.radius = CIRCLE1_RADIUS;

        // calculate the remaining attributes for circle1 here
        circle1.diameter = 2 * circle1.radius;
        circle1.circumference = Math.PI * circle1.diameter;
        circle1.area = Math.PI * Math.pow(circle1.radius, 2);


        Circle circle2 = new Circle();
        circle2.radius = CIRCLE2_RADIUS;

        // calculate the remaining attributes for circle2 here
        circle2.diameter = 2 * circle2.radius;
        circle2.circumference = Math.PI * circle2.diameter;
        circle2.area = Math.PI * Math.pow(circle2.radius, 2);

        // rounding to three decimal values when printing
        System.out.println("circle1.radius = " + Math.round(circle1.radius * ROUNDING_FACTOR) / ROUNDING_FACTOR);
        
        // add more output-commands here
        System.out.println("circle1.diameter = " + Math.round(circle1.diameter * ROUNDING_FACTOR) / ROUNDING_FACTOR);
        System.out.println("circle1.circumference = " + Math.round(circle1.circumference * ROUNDING_FACTOR) / ROUNDING_FACTOR);
        System.out.println("circle1.area = " + Math.round(circle1.area * ROUNDING_FACTOR) / ROUNDING_FACTOR);

        System.out.println("circle2.radius = " + Math.round(circle2.radius * ROUNDING_FACTOR) / ROUNDING_FACTOR);
        System.out.println("circle2.diameter = " + Math.round(circle2.diameter * ROUNDING_FACTOR) / ROUNDING_FACTOR);
        System.out.println("circle2.circumference = " + Math.round(circle2.circumference * ROUNDING_FACTOR) / ROUNDING_FACTOR);
        System.out.println("circle2.area = " + Math.round(circle2.area * ROUNDING_FACTOR) / ROUNDING_FACTOR);

        // cone objects
        Cone cone1 = new Cone();
        cone1.base = circle1;
        cone1.height = CONE_HEIGHT;
        cone1.volume = VOLUME_CALCULATION_FACTOR * Math.PI * Math.pow(cone1.base.radius, 2) * cone1.height;

        //implementation of slantHeight attribute (to make surfaceArea calculation more readable)
        cone1.slantHeight = Math.sqrt(Math.pow(cone1.base.radius, 2) + Math.pow(cone1.height, 2));
        cone1.surfaceArea = cone1.base.area + Math.PI * cone1.base.radius * cone1.slantHeight;

        Cone cone2 = new Cone();
        cone2.base = circle2;
        cone2.height = CONE_HEIGHT;
        cone2.volume = VOLUME_CALCULATION_FACTOR * Math.PI * Math.pow(cone2.base.radius, 2) * cone2.height;

        cone2.slantHeight = Math.sqrt(Math.pow(cone2.base.radius, 2) + Math.pow(cone2.height, 2));
        cone2.surfaceArea = cone2.base.area + Math.PI * cone2.base.radius * cone2.slantHeight;

        // printing cone values
        System.out.println("cone1.volume = " + Math.round(cone1.volume * ROUNDING_FACTOR) / ROUNDING_FACTOR);
        System.out.println("cone1.surfaceArea = " + Math.round(cone1.surfaceArea * ROUNDING_FACTOR) / ROUNDING_FACTOR);
        System.out.println("cone2.volume = " + Math.round(cone2.volume * ROUNDING_FACTOR) / ROUNDING_FACTOR);
        System.out.println("cone2.surfaceArea = " + Math.round(cone2.surfaceArea * ROUNDING_FACTOR) / ROUNDING_FACTOR);
    }

}
