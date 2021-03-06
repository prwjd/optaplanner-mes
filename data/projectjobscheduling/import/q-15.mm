************************************************************************
file with basedata            :
initial value random generator: 6885258911
************************************************************************
projects                      : 1
jobs (incl. supersource/sink ): 26
horizon                       : 0
RESOURCES
  - renewable                 : 26 R
  - nonrenewable              :  0 N
  - doubly constrained        :  0 D
************************************************************************
PROJECT INFORMATION:
pronr.  #jobs rel.date duedate tardcost  MPM-Time
    1      24        0       0        0         0
************************************************************************
PRECEDENCE RELATIONS:
jobnr.    #modes  #successors   successors
    1          1            4   2 3 4 5
    2          2            1   8
    3          2            2   10 11
    4          2            2   7 6
    5          2            1   9
    6          1            1   13
    7          1            1   13
    8          1            1   15
    9          1            1   14
   10          1            1   12
   11          1            1   12
   12         10            1   19
   13         10            1   18
   14         10            1   20
   15         10            3   16 17 21
   16          1            1   25
   17          1            1   25
   18          1            1   23
   19          1            1   22
   20          1            1   24
   21          1            1   25
   22          3            1   26
   23          3            1   26
   24          3            1   26
   25          2            1   26
   26          1            0
************************************************************************
REQUESTS/DURATIONS:
jobnr. mode duration       R 1   R 2   R 3   R 4   R 5   R 6   R 7   R 8   R 9   R 10   R 11   R 12   R 13   R 14   R 15   R 16   R 17   R 18   R 19   R 20   R 21   R 22   R 23   R 24   R 25   R 26
------------------------------------------------------------------------
    1     1        0         0     0     0     0     0     0     0     0     0      0      0      0      0      0      0      0      0      0      0      0      0      0      0      0      0      0
    2     1       26         1     0     0     0     0     0     0     0     0      0      0      0      0      0      0      0      0      0      0      0      0      0      0      0      0      0
          2       26         0     1     0     0     0     0     0     0     0      0      0      0      0      0      0      0      0      0      0      0      0      0      0      0      0      0
    3     1      180         1     0     0     0     0     0     0     0     0      0      0      0      0      0      0      0      0      0      0      0      0      0      0      0      0      0
          2      160         0     1     0     0     0     0     0     0     0      0      0      0      0      0      0      0      0      0      0      0      0      0      0      0      0      0
    4     1      180         1     0     0     0     0     0     0     0     0      0      0      0      0      0      0      0      0      0      0      0      0      0      0      0      0      0
          2      160         0     1     0     0     0     0     0     0     0      0      0      0      0      0      0      0      0      0      0      0      0      0      0      0      0      0
    5     1       74         1     0     0     0     0     0     0     0     0      0      0      0      0      0      0      0      0      0      0      0      0      0      0      0      0      0
          2       65         0     1     0     0     0     0     0     0     0      0      0      0      0      0      0      0      0      0      0      0      0      0      0      0      0      0
    6     1       53         0     0     1     0     0     0     0     0     0      0      0      0      0      0      0      0      0      0      0      0      0      0      0      0      0      0
    7     1       17         0     0     1     0     0     0     0     0     0      0      0      0      0      0      0      0      0      0      0      0      0      0      0      0      0      0
    8     1       17         0     0     1     0     0     0     0     0     0      0      0      0      0      0      0      0      0      0      0      0      0      0      0      0      0      0
    9     1       29         0     0     1     0     0     0     0     0     0      0      0      0      0      0      0      0      0      0      0      0      0      0      0      0      0      0
   10     1       17         0     0     1     0     0     0     0     0     0      0      0      0      0      0      0      0      0      0      0      0      0      0      0      0      0      0
   11     1       53         0     0     1     0     0     0     0     0     0      0      0      0      0      0      0      0      0      0      0      0      0      0      0      0      0      0
   12     1      400         0     0     0     1     0     0     0     0     0      0      0      0      0      0      0      0      0      0      0      0      0      0      0      0      0      0
          2      400         0     0     0     0     1     0     0     0     0      0      0      0      0      0      0      0      0      0      0      0      0      0      0      0      0      0
          3      400         0     0     0     0     0     1     0     0     0      0      0      0      0      0      0      0      0      0      0      0      0      0      0      0      0      0
          4      400         0     0     0     0     0     0     1     0     0      0      0      0      0      0      0      0      0      0      0      0      0      0      0      0      0      0
          5      400         0     0     0     0     0     0     0     1     0      0      0      0      0      0      0      0      0      0      0      0      0      0      0      0      0      0
          6      400         0     0     0     0     0     0     0     0     1      0      0      0      0      0      0      0      0      0      0      0      0      0      0      0      0      0
          7      400         0     0     0     0     0     0     0     0     0      1      0      0      0      0      0      0      0      0      0      0      0      0      0      0      0      0
          8      312         0     0     0     0     0     0     0     0     0      0      1      0      0      0      0      0      0      0      0      0      0      0      0      0      0      0
          9      400         0     0     0     0     0     0     0     0     0      0      0      1      0      0      0      0      0      0      0      0      0      0      0      0      0      0
         10      234         0     0     0     0     0     0     0     0     0      0      0      0      1      0      0      0      0      0      0      0      0      0      0      0      0      0
   13     1      400         0     0     0     1     0     0     0     0     0      0      0      0      0      0      0      0      0      0      0      0      0      0      0      0      0      0
          2      400         0     0     0     0     1     0     0     0     0      0      0      0      0      0      0      0      0      0      0      0      0      0      0      0      0      0
          3      400         0     0     0     0     0     1     0     0     0      0      0      0      0      0      0      0      0      0      0      0      0      0      0      0      0      0
          4      400         0     0     0     0     0     0     1     0     0      0      0      0      0      0      0      0      0      0      0      0      0      0      0      0      0      0
          5      400         0     0     0     0     0     0     0     1     0      0      0      0      0      0      0      0      0      0      0      0      0      0      0      0      0      0
          6      400         0     0     0     0     0     0     0     0     1      0      0      0      0      0      0      0      0      0      0      0      0      0      0      0      0      0
          7      400         0     0     0     0     0     0     0     0     0      1      0      0      0      0      0      0      0      0      0      0      0      0      0      0      0      0
          8      312         0     0     0     0     0     0     0     0     0      0      1      0      0      0      0      0      0      0      0      0      0      0      0      0      0      0
          9      400         0     0     0     0     0     0     0     0     0      0      0      1      0      0      0      0      0      0      0      0      0      0      0      0      0      0
         10      234         0     0     0     0     0     0     0     0     0      0      0      0      1      0      0      0      0      0      0      0      0      0      0      0      0      0
   14     1      163         0     0     0     1     0     0     0     0     0      0      0      0      0      0      0      0      0      0      0      0      0      0      0      0      0      0
          2      163         0     0     0     0     1     0     0     0     0      0      0      0      0      0      0      0      0      0      0      0      0      0      0      0      0      0
          3      163         0     0     0     0     0     1     0     0     0      0      0      0      0      0      0      0      0      0      0      0      0      0      0      0      0      0
          4      163         0     0     0     0     0     0     1     0     0      0      0      0      0      0      0      0      0      0      0      0      0      0      0      0      0      0
          5      163         0     0     0     0     0     0     0     1     0      0      0      0      0      0      0      0      0      0      0      0      0      0      0      0      0      0
          6      163         0     0     0     0     0     0     0     0     1      0      0      0      0      0      0      0      0      0      0      0      0      0      0      0      0      0
          7      163         0     0     0     0     0     0     0     0     0      1      0      0      0      0      0      0      0      0      0      0      0      0      0      0      0      0
          8      127         0     0     0     0     0     0     0     0     0      0      1      0      0      0      0      0      0      0      0      0      0      0      0      0      0      0
          9      163         0     0     0     0     0     0     0     0     0      0      0      1      0      0      0      0      0      0      0      0      0      0      0      0      0      0
         10       96         0     0     0     0     0     0     0     0     0      0      0      0      1      0      0      0      0      0      0      0      0      0      0      0      0      0
   15     1       80         0     0     0     1     0     0     0     0     0      0      0      0      0      0      0      0      0      0      0      0      0      0      0      0      0      0
          2       80         0     0     0     0     1     0     0     0     0      0      0      0      0      0      0      0      0      0      0      0      0      0      0      0      0      0
          3       80         0     0     0     0     0     1     0     0     0      0      0      0      0      0      0      0      0      0      0      0      0      0      0      0      0      0
          4       80         0     0     0     0     0     0     1     0     0      0      0      0      0      0      0      0      0      0      0      0      0      0      0      0      0      0
          5       80         0     0     0     0     0     0     0     1     0      0      0      0      0      0      0      0      0      0      0      0      0      0      0      0      0      0
          6       80         0     0     0     0     0     0     0     0     1      0      0      0      0      0      0      0      0      0      0      0      0      0      0      0      0      0
          7       62         0     0     0     0     0     0     0     0     0      1      0      0      0      0      0      0      0      0      0      0      0      0      0      0      0      0
          8       62         0     0     0     0     0     0     0     0     0      0      1      0      0      0      0      0      0      0      0      0      0      0      0      0      0      0
          9       80         0     0     0     0     0     0     0     0     0      0      0      1      0      0      0      0      0      0      0      0      0      0      0      0      0      0
         10       47         0     0     0     0     0     0     0     0     0      0      0      0      1      0      0      0      0      0      0      0      0      0      0      0      0      0
   16     1        8         0     0     0     0     0     0     0     0     0      0      0      0      0      1      0      0      0      0      0      0      0      0      0      0      0      0
   17     1        8         0     0     0     0     0     0     0     0     0      0      0      0      0      1      0      0      0      0      0      0      0      0      0      0      0      0
   18     1       70         0     0     0     0     0     0     0     0     0      0      0      0      0      1      0      0      0      0      0      0      0      0      0      0      0      0
   19     1       70         0     0     0     0     0     0     0     0     0      0      0      0      0      1      0      0      0      0      0      0      0      0      0      0      0      0
   20     1       29         0     0     0     0     0     0     0     0     0      0      0      0      0      1      0      0      0      0      0      0      0      0      0      0      0      0
   21     1        2         0     0     0     0     0     0     0     0     0      0      0      0      0      1      0      0      0      0      0      0      0      0      0      0      0      0
   22     1      466         0     0     0     0     0     0     0     0     0      0      0      0      0      0      1      0      0      0      0      0      0      0      0      0      0      0
          2      466         0     0     0     0     0     0     0     0     0      0      0      0      0      0      0      1      0      0      0      0      0      0      0      0      0      0
          3      466         0     0     0     0     0     0     0     0     0      0      0      0      0      0      0      0      1      0      0      0      0      0      0      0      0      0
   23     1      466         0     0     0     0     0     0     0     0     0      0      0      0      0      0      1      0      0      0      0      0      0      0      0      0      0      0
          2      466         0     0     0     0     0     0     0     0     0      0      0      0      0      0      0      1      0      0      0      0      0      0      0      0      0      0
          3      466         0     0     0     0     0     0     0     0     0      0      0      0      0      0      0      0      1      0      0      0      0      0      0      0      0      0
   24     1     1901         0     0     0     0     0     0     0     0     0      0      0      0      0      0      1      0      0      0      0      0      0      0      0      0      0      0
          2      456         0     0     0     0     0     0     0     0     0      0      0      0      0      0      0      0      0      0      1      0      0      0      0      0      0      0
          3      261         0     0     0     0     0     0     0     0     0      0      0      0      0      0      0      0      0      0      0      0      0      1      0      0      0      0
   25     1     1652         0     0     0     0     0     0     0     0     0      0      0      0      0      0      1      0      0      0      0      0      0      0      0      0      0      0
          2      283         0     0     0     0     0     0     0     0     0      0      0      0      0      0      0      0      0      0      0      0      0      0      0      0      1      0
   26     1        0         0     0     0     0     0     0     0     0     0      0      0      0      0      0      0      0      0      0      0      0      0      0      0      0      0      0
************************************************************************
RESOURCEAVAILABILITIES:
   R 1   R 2   R 3   R 4   R 5   R 6   R 7   R 8   R 9   R 10   R 11   R 12   R 13   R 14   R 15   R 16   R 17   R 18   R 19   R 20   R 21   R 22   R 23   R 24   R 25   R 26
     0     0     0     0     0     0     0     0     0      0      0      0      0      0      0      0      0      0      0      0      0      0      0      0      0      0
************************************************************************




MES extensions...
To force standard mode and preserve other mes mappings (scheduling_id, operations, machines)
remove or spoil any allocation in JOBALLOCATIONS section (some content between -----... and bottom *****...).


JOBALLOCATIONS:
jobNumber    executionModeNumber    predecessorsDoneDate           delay
------------------------------------------------------------------------
        1                      1                       0               0
        2                      2                       0             160
        3                      2                       0             190
        4                      2                       0               0
        5                      1                       0               0
        6                      1                     160              20
        7                      1                     160               0
        8                      1                     186              80
        9                      1                      74             160
       10                      1                     350               0
       11                      1                     350              20
       12                     10                     423               0
       13                      5                     233               0
       14                      3                     263               0
       15                      2                     283               0
       16                      1                     363              20
       17                      1                     363              10
       18                      1                     633             100
       19                      1                     657               0
       20                      1                     426               0
       21                      1                     363               0
       22                      2                     727               0
       23                      1                     803               0
       24                      2                     455               0
       25                      2                     391               0
       26                      1                    1269               0
************************************************************************


MESSCHEDULINGSESSION:
schedulingId           timeScale
------------------------------------------------------------------------
          15                1440
************************************************************************


JOB2MESOPERATION:
jobNumber            operationId     operationNr
------------------------------------------------------------------------
        2                  26144     PRA/2014/09/014460-26144
        3                  26138     PRA/2014/09/014495-26138
        4                  26134     PRA/2014/09/014485-26134
        5                  26130     PRA/2014/09/014483-26130
        6                  26140     ODG/PRA/2014/09/019209-26140
        7                  26135     ODG/PRA/2014/09/019092-26135
        8                  26145     ODG/PRA/2014/09/019067-26145
        9                  26131     ODG/PRA/2014/09/019090-26131
       10                  26139     ODG/PRA/2014/09/019102-26139
       11                  26141     ODG/PRA/2014/09/019213-26141
       12                  26137     MIE/2014/09/012854-26137
       13                  26133     MIE/2014/09/012844-26133
       14                  26129     MIE/2014/09/012842-26129
       15                  26143     MIE/2014/09/012819-26143
       16                  26142     ODG/MIE/2014/09/033344-26142
       17                  26155     ODG/MIE/2014/09/033761-26155
       18                  26132     ODG/MIE/2014/09/033369-26132
       19                  26136     ODG/MIE/2014/09/033379-26136
       20                  26128     ODG/MIE/2014/09/033367-26128
       21                  26156     ODG/MIE/2014/09/033762-26156
       22                  26024     PAK/2014/09/004887-26024
       23                  26023     PAK/2014/09/004886-26023
       24                  26020     PAK/2014/09/004883-26020
       25                  26078     PAK/2014/09/004941-26078
************************************************************************


RESOURCE2MESMACHINE:
resNumber              machineId     machineNr
------------------------------------------------------------------------
        1                    101     PIEC_01
        2                    102     PIEC_02
        3                    113     VT_SILOS
        4                    103     MLYN_01
        5                    104     MLYN_02
        6                    105     MLYN_03
        7                    106     MLYN_04
        8                    107     MLYN_05
        9                    108     MLYN_06
       10                    109     MLYN_07
       11                    110     MLYN_08
       12                    111     MLYN_09
       13                    112     MLYN_10
       14                    114     VT_WOZEK
       15                      4     Linia_01
       16                      5     Linia_03
       17                      6     Linia_02
       18                      7     Linia_04
       19                      8     Linia_05
       20                     10     Linia_06
       21                     11     Linia_07
       22                     12     Linia_08
       23                     13     Linia_09
       24                     14     Linia_10
       25                     15     Linia_11
       26                    236     Linia_12
************************************************************************


SCOREHIERARCHY:
scoreCode scoreType scoreName scoreDescription scoreLevel
------------------------------------------------------------------------
************************************************************************


