package cpu.present;

public interface PresentType {;

    enum BIN implements PresentType {
        TWOS_COMPLEMENT,
        FLOAT
    }

    enum DEC implements PresentType {
        INTEGER,
        FLOAT
    }

    enum BCD implements PresentType {
        W8421
    }

}
