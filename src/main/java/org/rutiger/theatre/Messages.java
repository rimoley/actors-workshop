package org.rutiger.theatre;

public class Messages {
    public static class Attack {}

    public static class Shot {
        private Integer arrows;
        public Shot(Integer arrows) {
            this.arrows = arrows;
        }

        public Integer arrows() {
            return arrows;
        }
    }
}
