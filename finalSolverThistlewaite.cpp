#include <bits/stdc++.h>
using namespace std;
typedef vector<int8_t> EdgePos;
typedef vector<int8_t> CornerPos;
typedef vector<int8_t> edgeOrientation;
typedef vector<int8_t> cornerOrientation;
typedef pair<EdgePos, edgeOrientation> Edges;
typedef pair<CornerPos, cornerOrientation> Corners;
typedef pair<Edges, Corners> Cube;
unordered_map<char, EdgePos> nextEdgePos = {{'R', {0, 5, 2, 3, 4, 9, 1, 7, 8, 6, 10, 11}},
                                            {'U', {1, 2, 3, 0, 4, 5, 6, 7, 8, 9, 10, 11}},
                                            {'F', {0, 1, 6, 3, 4, 5, 10, 2, 8, 9, 7, 11}},
                                            {'B', {4, 1, 2, 3, 8, 0, 6, 7, 5, 9, 10, 11}},
                                            {'L', {0, 1, 2, 7, 3, 5, 6, 11, 8, 9, 10, 4}},
                                            {'D', {0, 1, 2, 3, 4, 5, 6, 7, 11, 8, 9, 10}},
                                            {'E', {0, 1, 2, 3, 7, 4, 5, 6, 8, 9, 10, 11}}};
unordered_map<char, CornerPos> nextCornerPos = {{'R', {0, 5, 1, 3, 4, 6, 2, 7}},
                                                {'U', {1, 2, 3, 0, 4, 5, 6, 7}},
                                                {'F', {0, 1, 6, 2, 4, 5, 7, 3}},
                                                {'B', {4, 0, 2, 3, 5, 1, 6, 7}},
                                                {'L', {3, 1, 2, 7, 0, 5, 6, 4}},
                                                {'D', {0, 1, 2, 3, 7, 4, 5, 6}}};
unordered_map<char, vector<map<int8_t, int8_t>>> nextEdgeOrientation = {{'R', {{{3, 3}, {2, 2}}, {{3, 2}, {1, 1}}, {{3, 3}, {-2, -2}}, {{3, 3}, {-1, -1}}, {{2, 2}, {-1, -1}}, {{2, -3}, {1, 1}}, {{-2, 3}, {1, 1}}, {{-2, -2}, {-1, -1}}, {{2, 2}, {-3, -3}}, {{1, 1}, {-3, -2}}, {{-3, -3}, {-2, -2}}, {{-3, -3}, {-1, -1}}}},
                                                                        {'U', {{{3, 3}, {2, 1}}, {{3, 3}, {1, -2}}, {{3, 3}, {-2, -1}}, {{3, 3}, {-1, 2}}, {{2, 2}, {-1, -1}}, {{2, 2}, {1, 1}}, {{-2, -2}, {1, 1}}, {{-2, -2}, {-1, -1}}, {{2, 2}, {-3, -3}}, {{1, 1}, {-3, -3}}, {{-3, -3}, {-2, -2}}, {{-3, -3}, {-1, -1}}}},
                                                                        {'F', {{{3, 3}, {2, 2}}, {{3, 3}, {1, 1}}, {{3, 1}, {-2, -2}}, {{3, 3}, {-1, -1}}, {{2, 2}, {-1, -1}}, {{2, 2}, {1, 1}}, {{-2, -2}, {1, -3}}, {{-2, -2}, {-1, 3}}, {{2, 2}, {-3, -3}}, {{1, 1}, {-3, -3}}, {{-3, -1}, {-2, -2}}, {{-3, -3}, {-1, -1}}}},
                                                                        {'B', {{{3, -1}, {2, 2}}, {{3, 3}, {1, 1}}, {{3, 3}, {-2, -2}}, {{3, 3}, {-1, -1}}, {{2, 2}, {-1, -3}}, {{2, 2}, {1, 3}}, {{-2, -2}, {1, 1}}, {{-2, -2}, {-1, -1}}, {{2, 2}, {-3, 1}}, {{1, 1}, {-3, -3}}, {{-3, -3}, {-2, -2}}, {{-3, -3}, {-1, -1}}}},
                                                                        {'L', {{{3, 3}, {2, 2}}, {{3, 3}, {1, 1}}, {{3, 3}, {-2, -2}}, {{3, -2}, {-1, -1}}, {{2, 3}, {-1, -1}}, {{2, 2}, {1, 1}}, {{-2, -2}, {1, 1}}, {{-2, -3}, {-1, -1}}, {{2, 2}, {-3, -3}}, {{1, 1}, {-3, -3}}, {{-3, -3}, {-2, -2}}, {{-3, 2}, {-1, -1}}}},
                                                                        {'D', {{{3, 3}, {2, 2}}, {{3, 3}, {1, 1}}, {{3, 3}, {-2, -2}}, {{3, 3}, {-1, -1}}, {{2, 2}, {-1, -1}}, {{2, 2}, {1, 1}}, {{-2, -2}, {1, 1}}, {{-2, -2}, {-1, -1}}, {{2, -1}, {-3, -3}}, {{1, 2}, {-3, -3}}, {{-3, -3}, {-2, 1}}, {{-3, -3}, {-1, -2}}}}};

unordered_map<char, vector<map<int8_t, int8_t>>> nextCornerOrientation = {{'R', {{{-1, -1}, {2, 2}, {3, 3}}, {{1, 1}, {2, -3}, {3, 2}}, {{1, 1}, {-2, 3}, {3, 2}}, {{-1, -1}, {-2, -2}, {3, 3}}, {{-1, -1}, {2, 2}, {-3, -3}}, {{1, 1}, {2, -3}, {-3, -2}}, {{1, 1}, {-2, 3}, {-3, -2}}, {{-1, -1}, {-2, -2}, {-3, -3}}}},
                                                                          {'U', {{{-1, 2}, {2, 1}, {3, 3}}, {{1, -2}, {2, 1}, {3, 3}}, {{1, -2}, {-2, -1}, {3, 3}}, {{-1, 2}, {-2, -1}, {3, 3}}, {{-1, -1}, {2, 2}, {-3, -3}}, {{1, 1}, {2, 2}, {-3, -3}}, {{1, 1}, {-2, -2}, {-3, -3}}, {{-1, -1}, {-2, -2}, {-3, -3}}}},
                                                                          {'F', {{{-1, -1}, {2, 2}, {3, 3}}, {{1, 1}, {2, 2}, {3, 3}}, {{1, -3}, {-2, -2}, {3, 1}}, {{-1, 3}, {-2, -2}, {3, 1}}, {{-1, -1}, {2, 2}, {-3, -3}}, {{1, 1}, {2, 2}, {-3, -3}}, {{1, -3}, {-2, -2}, {-3, -1}}, {{-1, 3}, {-2, -2}, {-3, -1}}}},
                                                                          {'B', {{{-1, -3}, {2, 2}, {3, -1}}, {{1, 3}, {2, 2}, {3, -1}}, {{1, 1}, {-2, -2}, {3, 3}}, {{-1, -1}, {-2, -2}, {3, 3}}, {{-1, -3}, {2, 2}, {-3, 1}}, {{1, 3}, {2, 2}, {-3, 1}}, {{1, 1}, {-2, -2}, {-3, -3}}, {{-1, -1}, {-2, -2}, {-3, -3}}}},
                                                                          {'L', {{{-1, -1}, {2, 3}, {3, -2}}, {{1, 1}, {2, 2}, {3, 3}}, {{1, 1}, {-2, -2}, {3, 3}}, {{-1, -1}, {-2, -3}, {3, -2}}, {{-1, -1}, {2, 3}, {-3, 2}}, {{1, 1}, {2, 2}, {-3, -3}}, {{1, 1}, {-2, -2}, {-3, -3}}, {{-1, -1}, {-2, -3}, {-3, 2}}}},
                                                                          {'D', {{{-1, -1}, {2, 2}, {3, 3}}, {{1, 1}, {2, 2}, {3, 3}}, {{1, 1}, {-2, -2}, {3, 3}}, {{-1, -1}, {-2, -2}, {3, 3}}, {{-1, -2}, {2, -1}, {-3, -3}}, {{1, 2}, {2, -1}, {-3, -3}}, {{1, 2}, {-2, 1}, {-3, -3}}, {{-1, -2}, {-2, 1}, {-3, -3}}}}};
vector<vector<int8_t>> parityEdgeValues = {{1, 0, 1, 0, 0, 0, 0, 0, 1, 0, 1, 0},
                                           {0, 1, 0, 1, 0, 0, 0, 0, 0, 1, 0, 1},
                                           {1, 0, 1, 0, 0, 0, 0, 0, 1, 0, 1, 0},
                                           {0, 1, 0, 1, 0, 0, 0, 0, 0, 1, 0, 1},
                                           {0, 0, 0, 0, 1, 1, 1, 1, 0, 0, 0, 0},
                                           {0, 0, 0, 0, 1, 1, 1, 1, 0, 0, 0, 0},
                                           {0, 0, 0, 0, 1, 1, 1, 1, 0, 0, 0, 0},
                                           {0, 0, 0, 0, 1, 1, 1, 1, 0, 0, 0, 0},
                                           {1, 0, 1, 0, 0, 0, 0, 0, 1, 0, 1, 0},
                                           {0, 1, 0, 1, 0, 0, 0, 0, 0, 1, 0, 1},
                                           {1, 0, 1, 0, 0, 0, 0, 0, 1, 0, 1, 0},
                                           {0, 1, 0, 1, 0, 0, 0, 0, 0, 1, 0, 1}};
vector<vector<int8_t>> parityCornerValues = {{1, 0, 1, 0, 0, 1, 0, 1},
                                             {0, 1, 0, 1, 1, 0, 1, 0},
                                             {1, 0, 1, 0, 0, 1, 0, 1},
                                             {0, 1, 0, 1, 1, 0, 1, 0},
                                             {0, 1, 0, 1, 1, 0, 1, 0},
                                             {1, 0, 1, 0, 0, 1, 0, 1},
                                             {0, 1, 0, 1, 1, 0, 1, 0},
                                             {1, 0, 1, 0, 0, 1, 0, 1}};
EdgePos SOLVED_EDGE_POS = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11};
CornerPos SOLVED_CORNER_POS = {0, 1, 2, 3, 4, 5, 6, 7};
edgeOrientation SOLVED_EDGE_ORIENTATION = {3, 3, 3, 3, 2, 2, -2, -2, -3, -3, -3, -3};
cornerOrientation SOLVED_CORNER_ORIENTATION = {3, 3, 3, 3, -3, -3, -3, -3};
unordered_map<int8_t, int8_t> edgePosForNumber = {{0, 0b100010}, {1, 0b100100}, {2, 0b101000}, {3, 0b110000}, {4, 0b010010}, {5, 0b000110}, {6, 0b001100}, {7, 0b011000}, {8, 0b000011}, {9, 0b000101}, {10, 0b001001}, {11, 0b010001}};
unordered_map<int8_t, vector<int8_t>> edgeIndicesForPos = {{0b100010, {1, 37}}, {0b100100, {5, 28}}, {0b101000, {7, 19}}, {0b110000, {3, 10}}, {0b010010, {12, 41}}, {0b000110, {32, 39}}, {0b001100, {23, 30}}, {0b011000, {14, 21}}, {0b000011, {43, 52}}, {0b000101, {34, 50}}, {0b001001, {25, 46}}, {0b010001, {16, 48}}};
unordered_map<int8_t, int8_t> cornerPosForNumber = {{0, 0b110010}, {1, 0b100110}, {2, 0b101100}, {3, 0b111000}, {4, 0b010011}, {5, 0b000111}, {6, 0b001101}, {7, 0b011001}};
unordered_map<int8_t, vector<int8_t>> cornerIndicesForPos = {{0b110010, {0, 9, 38}}, {0b100110, {2, 29, 36}}, {0b101100, {8, 20, 27}}, {0b111000, {6, 11, 18}}, {0b010011, {15, 44, 51}}, {0b000111, {35, 42, 53}}, {0b001101, {26, 33, 47}}, {0b011001, {17, 24, 45}}};
unordered_map<int8_t, int8_t> edgeNumberForPos = {{0b100010, 0}, {0b100100, 1}, {0b101000, 2}, {0b110000, 3}, {0b010010, 4}, {0b000110, 5}, {0b001100, 6}, {0b011000, 7}, {0b000011, 8}, {0b000101, 9}, {0b001001, 10}, {0b010001, 11}};
unordered_map<int8_t, int8_t> cornerNumberForPos = {{0b110010, 0}, {0b100110, 1}, {0b101100, 2}, {0b111000, 3}, {0b010011, 4}, {0b000111, 5}, {0b001101, 6}, {0b011001, 7}};
Cube colorToMatrix(string colorInput)
{
    Cube c = {{SOLVED_EDGE_POS, SOLVED_EDGE_ORIENTATION}, {SOLVED_CORNER_POS, SOLVED_CORNER_ORIENTATION}};
    string basicPositions = "ULFRBD", givenPositions = "";
    vector<int8_t> basicPositionsInfo = {4, 13, 22, 31, 40, 49}, basicOrientationInfo = {3, -1, -2, 1, 2, -3};
    for (auto i : basicPositionsInfo)
        givenPositions += colorInput[i];
    for (int8_t i = 0; i < 12; i++)
    {
        int8_t x = 0;
        for (auto j : givenPositions)
        {
            int8_t flag = 0;
            for (auto k : edgeIndicesForPos[edgePosForNumber[i]])
                if (colorInput[k] == j)
                    flag = 1;
            x = 2 * x + flag;
        }
        c.first.first[edgeNumberForPos[x]] = i;
    }
    for (int8_t i = 0; i < 8; i++)
    {
        int8_t x = 0;
        for (auto j : givenPositions)
        {
            int8_t flag = 0;
            for (auto k : cornerIndicesForPos[cornerPosForNumber[i]])
                if (colorInput[k] == j)
                    flag = 1;
            x = 2 * x + flag;
        }
        c.second.first[cornerNumberForPos[x]] = i;
    }
    for (int8_t i = 0; i < 4; i++)
        for (auto j : edgeIndicesForPos[edgePosForNumber[c.first.first[i]]])
            if (colorInput[j] == givenPositions[0])
                c.first.second[i] = basicOrientationInfo[j / 9];
    for (int8_t i = 4; i < 8; i++)
        for (auto j : edgeIndicesForPos[edgePosForNumber[c.first.first[i]]])
            if (colorInput[j] == givenPositions[2] || colorInput[j] == givenPositions[4])
                c.first.second[i] = basicOrientationInfo[j / 9];
    for (int8_t i = 8; i < 12; i++)
        for (auto j : edgeIndicesForPos[edgePosForNumber[c.first.first[i]]])
            if (colorInput[j] == givenPositions[5])
                c.first.second[i] = basicOrientationInfo[j / 9];
    for (int8_t i = 0; i < 8; i++)
        for (auto j : cornerIndicesForPos[cornerPosForNumber[c.second.first[i]]])
            if (colorInput[j] == givenPositions[0] || colorInput[j] == givenPositions[5])
                c.second.second[i] = basicOrientationInfo[j / 9];
    return c;
}
Cube Execute(Cube c, string s)
{
    Cube temp = c;
    for (auto i : s)
    {
        for (int8_t j = 0; j < 12; j++)
            temp.first.second[j] = nextEdgeOrientation[i][temp.first.first[j]][temp.first.second[j]],
            temp.first.first[j] = nextEdgePos[i][temp.first.first[j]];
        for (int8_t j = 0; j < 8; j++)
            temp.second.second[j] = nextCornerOrientation[i][temp.second.first[j]][temp.second.second[j]],
            temp.second.first[j] = nextCornerPos[i][temp.second.first[j]];
    }
    return temp;
}
void printStatus(Cube c)
{
    cout << "{" << c.first.first[0];
    for (int8_t i = 1; i < 12; i++)
        cout << "," << c.first.first[i];
    cout << "}\t";
    cout << "{" << c.first.second[0];
    for (int8_t i = 1; i < 12; i++)
        cout << "," << c.first.second[i];
    cout << "}\t";
    cout << "{" << c.second.first[0];
    for (int8_t i = 1; i < 8; i++)
        cout << "," << c.second.first[i];
    cout << "}\t";
    cout << "{" << c.second.second[0];
    for (int8_t i = 1; i < 8; i++)
        cout << "," << c.second.second[i];
    cout << "}\n";
}
string reverseAlgorithm(string s)
{
    string result = "";
    for (auto i : s)
        for (int8_t j = 0; j < 3; j++)
            result += i;
    reverse(result.begin(), result.end());
    return result;
}
vector<int8_t> maskG1Condition(Cube c)
{
    vector<pair<int8_t, int8_t>> edgeMask(12);
    vector<int8_t> cornerMask(8), finalMask;
    for (int8_t i = 0; i < 4; i++)
        edgeMask[c.first.first[i]] = {0, c.first.second[i]};
    for (int8_t i = 4; i < 8; i++)
        edgeMask[c.first.first[i]] = {1, c.first.second[i]};
    for (int8_t i = 8; i < 12; i++)
        edgeMask[c.first.first[i]] = {0, c.first.second[i]};
    for (int8_t i = 0; i < 8; i++)
        cornerMask[c.second.first[i]] = c.second.second[i];
    for (auto i : edgeMask)
    {
        finalMask.push_back(i.first);
        finalMask.push_back(i.second);
    }
    for (auto i : cornerMask)
        finalMask.push_back(i);
    return finalMask;
}
string solveG1(Cube c)
{
    Cube source = c, destination = {{SOLVED_EDGE_POS, SOLVED_EDGE_ORIENTATION}, {SOLVED_CORNER_POS, SOLVED_CORNER_ORIENTATION}};
    vector<string> operations = {"R", "RR", "RRR", "U", "UU", "UUU", "F", "FF", "FFF", "L", "LL", "LLL", "B", "BB", "BBB", "D", "DD", "DDD"};
    queue<pair<Cube, string>> forward, backward;
    map<vector<int8_t>, string> f_sol, b_sol;
    f_sol[maskG1Condition(source)] = b_sol[maskG1Condition(destination)] = "";
    forward.push({source, ""});
    backward.push({destination, ""});
    while (true)
    {
        auto f = forward.front();
        forward.pop();
        for (auto i : operations)
        {
            Cube temp = Execute(f.first, i);
            vector<int8_t> x = maskG1Condition(temp);
            if (b_sol.count(x))
                return f.second + i + reverseAlgorithm(b_sol[x]);
            if (!f_sol.count(x))
            {
                f_sol[x] = f.second + i;
                forward.push({temp, f.second + i});
            }
        }
        auto b = backward.front();
        backward.pop();
        for (auto i : operations)
        {
            Cube temp = Execute(b.first, i);
            vector<int8_t> x = maskG1Condition(temp);
            if (f_sol.count(x))
                return f_sol[x] + reverseAlgorithm(b.second + i);
            if (!b_sol.count(x))
            {
                b_sol[x] = b.second + i;
                backward.push({temp, b.second + i});
            }
        }
    }
}
vector<int8_t> maskG2Condition(Cube c)
{
    vector<int8_t> result, parityEdges(12), parityCorners(8), parityCornerPos(8);
    for (int8_t i = 0; i < 12; i++)
        parityEdges[c.first.first[i]] = parityEdgeValues[c.first.first[i]][i];
    for (int8_t i = 0; i < 8; i++)
        parityCorners[c.second.first[i]] = parityCornerValues[c.second.first[i]][i], parityCornerPos[c.second.first[i]] = i / 4;
    for (int8_t i = 0; i < 4; i++)
        result.push_back(parityEdges[i]);
    for (int8_t i = 8; i < 12; i++)
        result.push_back(parityEdges[i]);
    for (auto i : parityCorners)
        result.push_back(i);
    for (auto i : parityCornerPos)
        result.push_back(i);
    for (auto i : c.second.first)
        result.push_back(i);
    return result;
}
string solveG2(Cube c)
{
    Cube source = c, destination = {{SOLVED_EDGE_POS, SOLVED_EDGE_ORIENTATION}, {SOLVED_CORNER_POS, SOLVED_CORNER_ORIENTATION}};
    vector<string> operations = {"U", "UU", "UUU", "D", "DD", "DDD", "RR", "FF", "LL", "BB"};
    queue<pair<Cube, string>> forward, backward;
    map<vector<int8_t>, string> f_sol, b_sol;
    f_sol[maskG2Condition(source)] = b_sol[maskG2Condition(destination)] = "";
    forward.push({source, ""});
    backward.push({destination, ""});
    while (true)
    {
        auto f = forward.front();
        forward.pop();
        for (auto i : operations)
        {
            Cube temp = Execute(f.first, i);
            vector<int8_t> x = maskG2Condition(temp);
            if (b_sol.count(x))
                return f.second + i + reverseAlgorithm(b_sol[x]);
            if (!f_sol.count(x))
            {
                f_sol[x] = f.second + i;
                forward.push({temp, f.second + i});
            }
        }
        auto b = backward.front();
        backward.pop();
        for (auto i : operations)
        {
            Cube temp = Execute(b.first, i);
            vector<int8_t> x = maskG2Condition(temp);
            if (f_sol.count(x))
                return f_sol[x] + reverseAlgorithm(b.second + i);
            if (!b_sol.count(x))
            {
                b_sol[x] = b.second + i;
                backward.push({temp, b.second + i});
            }
        }
    }
}
vector<int8_t> maskG3condition(Cube c)
{
    vector<int8_t> result;
    for (int8_t i = 0; i < 12; i++)
        result.push_back(c.first.first[i]);
    for (int8_t i = 0; i < 8; i++)
        result.push_back(c.second.first[i]);
    return result;
}
vector<int8_t> maskcondition(Cube c)
{
    vector<int8_t> v;
    for (auto i : c.first.first)
        v.push_back(i);
    for (auto i : c.second.first)
        v.push_back(i);
    return v;
}
string solveG3(Cube c)
{
    Cube source = c, destination = {{SOLVED_EDGE_POS, SOLVED_EDGE_ORIENTATION}, {SOLVED_CORNER_POS, SOLVED_CORNER_ORIENTATION}};
    vector<string> operations = {"UU", "DD", "RR", "FF", "LL", "BB"};
    queue<pair<Cube, string>> forward, backward;
    map<vector<int8_t>, string> f_sol, b_sol;
    f_sol[maskG3condition(source)] = b_sol[maskG3condition(destination)] = "";
    forward.push({source, ""});
    backward.push({destination, ""});
    while (true)
    {
        auto f = forward.front();
        forward.pop();
        for (auto i : operations)
        {
            Cube temp = Execute(f.first, i);
            vector<int8_t> x = maskG3condition(temp);
            if (b_sol.count(x))
                return f.second + i + reverseAlgorithm(b_sol[x]);
            if (!f_sol.count(x))
            {
                f_sol[x] = f.second + i;
                forward.push({temp, f.second + i});
            }
        }
        auto b = backward.front();
        backward.pop();
        for (auto i : operations)
        {
            Cube temp = Execute(b.first, i);
            vector<int8_t> x = maskG3condition(temp);
            if (f_sol.count(x))
                return f_sol[x] + reverseAlgorithm(b.second + i);
            if (!b_sol.count(x))
            {
                b_sol[x] = b.second + i;
                backward.push({temp, b.second + i});
            }
        }
    }
}
vector<string> alg(string moves)
{
    stack<pair<char, int8_t>> s;
    vector<string> v = {"", "", "2", "'"}, result;
    for (auto i : moves)
    {
        if (s.empty() || s.top().first != i)
            s.push({i, 1});
        else
        {
            auto x = s.top();
            s.pop();
            if (x.second != 3)
                s.push({x.first, x.second + 1});
        }
    }
    while (!s.empty())
    {
        auto x = s.top();
        if (x.second)
            result.insert(begin(result), x.first + v[x.second]);
        s.pop();
    }
    return result;
}
vector<string> splitString(string s)
{
    vector<string> result;
    result.push_back("");
    for (auto i : s)
    {
        if (i == ' ')
            result.push_back("");
        else
            result.back().push_back(i);
    }
    return result;
}
string ip(vector<string> s)
{
    string result = "";
    for (auto i : s)
    {
        if (i.size() == 1)
            result += i;
        else
        {
            if (i[1] == '\'')
                for (int8_t j = 0; j < 3; j++)
                    result += i[0];
            else
                for (int8_t j = 0; j < 2; j++)
                    result += i[0];
        }
    }
    return result;
}

vector<string> solveCube(Cube c)
{
    string G0_to_G1 = solveG1(c);
    c = Execute(c, G0_to_G1);
    string G1_to_G2 = solveG2(c);
    c = Execute(c, G1_to_G2);
    string G2_to_G3 = solveG3(c);
    return alg(G0_to_G1 + G1_to_G2 + G2_to_G3);
}
int main()
{
    string input;
    // cin >> input;
    getline(cin, input);
    Cube c = colorToMatrix(input);
    // input = ip(splitString(input));
    // Cube c = {{SOLVED_EDGE_POS, SOLVED_EDGE_ORIENTATION}, {SOLVED_CORNER_POS, SOLVED_CORNER_ORIENTATION}};
    // c = Execute(c, input);
    vector<string> result = solveCube(c);
    for (auto i : result)
        cout << i << " ";
    cout << endl
         << result.size() << endl;
}
// bbbbbbbobowooooobowgwwwwwwwrrrrrrrrryyyyyyyyygggoggggg

// bobywoggoobwobrbworwggyrrbwgywrggbwyrwywroggoyyborygrby
// bobywoggoobwobrbworwgyrrbwgywrggbwyrwywroggoyyborygrby