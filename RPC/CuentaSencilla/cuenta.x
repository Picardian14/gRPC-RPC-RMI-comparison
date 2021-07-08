struct operands {
    int x;
    int y;
};

program CUENTA {
    version CUENTA_VERSION {
        int SUMA(operands) = 1;
        int RESTA(operands) = 2;
    } = 1;
} = 0x20000001;