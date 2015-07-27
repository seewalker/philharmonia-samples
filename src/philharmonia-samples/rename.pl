use strict;

my %notes = ( 'A1' => 33, 'A2' => 45, 'A3' => 57,'A4' => 69,'A5' => 81, 'A6' => 93, 'A7'=> 105,
              'As0' => 22, 'As1' => 34, 'As2' => 46, 'As3'=> 58,'As4'=> 70,'As5'=> 82,'As6'=> 94, 'As7'=> 106,
              'B0' => 23, 'B1' => 35, 'B2' => 47, 'B3'=> 59,'B4'=> 71,'B5'=> 83,'B6'=> 95, 'B7'=> 107,
              'C1' => 24 , 'C2' => 36 , 'C3' => 48,'C4' => 60,'C5' => 72,'C6' => 84, 'C7' => 96, 'C8' => 108,
              'Cs1' => 25, 'Cs2' => 37, 'Cs3'=> 49,'Cs4'=> 61,'Cs5'=> 73,'Cs6'=> 85, 'Cs7'=> 97,
              'D1' => 26, 'D2' => 38, 'D3'=> 50,'D4'=> 62,'D5'=> 74,'D6'=> 86, 'D7'=> 98,
              'Ds1' => 27, 'Ds2' => 39, 'Ds3'=> 51,'Ds4'=> 63,'Ds5'=> 75,'Ds6'=> 87, 'Ds7'=> 99,
              'E1' => 28, 'E2' => 40, 'E3'=> 52,'E4'=> 64,'E5'=> 76,'E6'=> 88, 'E7'=> 100, 'E8' => 112,
              'F1' => 29, 'F2' => 41, 'F3'=> 53,'F4'=> 65,'F5'=> 77,'F6'=> 89, 'F7'=> 101,
              'Fs1' => 30, 'Fs2' => 42, 'Fs3'=> 54,'Fs4'=> 66,'Fs5'=> 78,'Fs6'=> 90, 'Fs7'=> 102,
              'G1' => 31, 'G2' => 43, 'G3'=> 55,'G4'=> 67,'G5'=> 79,'G6'=> 91, 'G7'=> 103,
              'Gs1' => 32, 'Gs2' => 44, 'Gs3'=> 56,'Gs4'=> 68,'Gs5'=> 80,'Gs6'=> 92, 'Gs7'=> 104);

sub elem {
    my $val = shift;
    for my $k (keys %notes) {
        return 1 if $val eq $k;
    }
    return 0;
}

my $rt = "/Users/shalom/myData/phil-samples"; 
opendir(my $dh, $rt);
my $inst;
my $sample;
while ($inst = (readdir $dh)) {
    next if $inst eq ".";
    next if $inst eq "..";
    opendir(my $idh, "$rt/$inst");
    while ($sample = (readdir $idh)) {
        next if $sample eq ".";
        next if $sample eq "..";
        my @parts = split('_', $sample);
        if (elem $parts[1]) {
            $parts[1] = $notes{$parts[1]};
            my $newsample = join('_',@parts);
            system "mv $rt/$inst/$sample $rt/$inst/$newsample";
        }
        else {
            print "nope $sample\n";
        }
    }
}
