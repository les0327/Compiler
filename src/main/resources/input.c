int a = 0;
int b = -3;
int c = 0;
int d = 0;

if (a != b) {
    c = 3;
} else if (a != (6 | 2)) {
    d = 5;
} else {
    b += 23;
}

switch (b) {
    case 0:
        a += 12;
        break;
    default:
        a -= b + c * (d + 12);
}

/*
aasda
asdsad
asdasd
*/