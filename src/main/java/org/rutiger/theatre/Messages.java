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

    public static class Killed {
        private Integer orcs;
        public Killed(Integer orcs) {
            this.orcs = orcs;
        }

        public Integer orcs() {
            return orcs;
        }
    }

    public static class ReinforcementsArrive {
        private Integer orcs;
        public ReinforcementsArrive(Integer orcs) {
            this.orcs = orcs;
        }

        public Integer orcs() {
            return orcs;
        }
    }

    public static class EndBattle {}
}
