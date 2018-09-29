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

    public static class Swung {
        private Integer axe;
        public Swung(Integer axe) {
            this.axe = axe;
        }

        public Integer axe() {
            return axe;
        }
    }
}
