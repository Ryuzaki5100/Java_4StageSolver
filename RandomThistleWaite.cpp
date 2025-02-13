#include <bits/stdc++.h>
using namespace std;
typedef vector<int> EdgePos;
typedef vector<int> CornerPos;
typedef vector<int> edgeOrientation;
typedef vector<int> cornerOrientation;
typedef pair<EdgePos, edgeOrientation> Edges;
typedef pair<CornerPos, cornerOrientation> Corners;
typedef pair<Edges, Corners> Cube;
map<char, EdgePos> nextEdgePos = {{'R', {0, 5, 2, 3, 4, 9, 1, 7, 8, 6, 10, 11}},
                                  {'U', {1, 2, 3, 0, 4, 5, 6, 7, 8, 9, 10, 11}},
                                  {'F', {0, 1, 6, 3, 4, 5, 10, 2, 8, 9, 7, 11}},
                                  {'B', {4, 1, 2, 3, 8, 0, 6, 7, 5, 9, 10, 11}},
                                  {'L', {0, 1, 2, 7, 3, 5, 6, 11, 8, 9, 10, 4}},
                                  {'D', {0, 1, 2, 3, 4, 5, 6, 7, 11, 8, 9, 10}},
                                  {'E', {0, 1, 2, 3, 7, 4, 5, 6, 8, 9, 10, 11}}};
map<char, CornerPos> nextCornerPos = {{'R', {0, 5, 1, 3, 4, 6, 2, 7}},
                                      {'U', {1, 2, 3, 0, 4, 5, 6, 7}},
                                      {'F', {0, 1, 6, 2, 4, 5, 7, 3}},
                                      {'B', {4, 0, 2, 3, 5, 1, 6, 7}},
                                      {'L', {3, 1, 2, 7, 0, 5, 6, 4}},
                                      {'D', {0, 1, 2, 3, 7, 4, 5, 6}}};
map<char, vector<map<int, int>>> nextEdgeOrientation = {{'R', {{{3, 3}, {2, 2}}, {{3, 2}, {1, 1}}, {{3, 3}, {-2, -2}}, {{3, 3}, {-1, -1}}, {{2, 2}, {-1, -1}}, {{2, -3}, {1, 1}}, {{-2, 3}, {1, 1}}, {{-2, -2}, {-1, -1}}, {{2, 2}, {-3, -3}}, {{1, 1}, {-3, -2}}, {{-3, -3}, {-2, -2}}, {{-3, -3}, {-1, -1}}}},
                                                        {'U', {{{3, 3}, {2, 1}}, {{3, 3}, {1, -2}}, {{3, 3}, {-2, -1}}, {{3, 3}, {-1, 2}}, {{2, 2}, {-1, -1}}, {{2, 2}, {1, 1}}, {{-2, -2}, {1, 1}}, {{-2, -2}, {-1, -1}}, {{2, 2}, {-3, -3}}, {{1, 1}, {-3, -3}}, {{-3, -3}, {-2, -2}}, {{-3, -3}, {-1, -1}}}},
                                                        {'F', {{{3, 3}, {2, 2}}, {{3, 3}, {1, 1}}, {{3, 1}, {-2, -2}}, {{3, 3}, {-1, -1}}, {{2, 2}, {-1, -1}}, {{2, 2}, {1, 1}}, {{-2, -2}, {1, -3}}, {{-2, -2}, {-1, 3}}, {{2, 2}, {-3, -3}}, {{1, 1}, {-3, -3}}, {{-3, -1}, {-2, -2}}, {{-3, -3}, {-1, -1}}}},
                                                        {'B', {{{3, -1}, {2, 2}}, {{3, 3}, {1, 1}}, {{3, 3}, {-2, -2}}, {{3, 3}, {-1, -1}}, {{2, 2}, {-1, -3}}, {{2, 2}, {1, 3}}, {{-2, -2}, {1, 1}}, {{-2, -2}, {-1, -1}}, {{2, 2}, {-3, 1}}, {{1, 1}, {-3, -3}}, {{-3, -3}, {-2, -2}}, {{-3, -3}, {-1, -1}}}},
                                                        {'L', {{{3, 3}, {2, 2}}, {{3, 3}, {1, 1}}, {{3, 3}, {-2, -2}}, {{3, -2}, {-1, -1}}, {{2, 3}, {-1, -1}}, {{2, 2}, {1, 1}}, {{-2, -2}, {1, 1}}, {{-2, -3}, {-1, -1}}, {{2, 2}, {-3, -3}}, {{1, 1}, {-3, -3}}, {{-3, -3}, {-2, -2}}, {{-3, 2}, {-1, -1}}}},
                                                        {'D', {{{3, 3}, {2, 2}}, {{3, 3}, {1, 1}}, {{3, 3}, {-2, -2}}, {{3, 3}, {-1, -1}}, {{2, 2}, {-1, -1}}, {{2, 2}, {1, 1}}, {{-2, -2}, {1, 1}}, {{-2, -2}, {-1, -1}}, {{2, -1}, {-3, -3}}, {{1, 2}, {-3, -3}}, {{-3, -3}, {-2, 1}}, {{-3, -3}, {-1, -2}}}}};

map<char, vector<map<int, int>>> nextCornerOrientation = {{'R', {{{-1, -1}, {2, 2}, {3, 3}}, {{1, 1}, {2, -3}, {3, 2}}, {{1, 1}, {-2, 3}, {3, 2}}, {{-1, -1}, {-2, -2}, {3, 3}}, {{-1, -1}, {2, 2}, {-3, -3}}, {{1, 1}, {2, -3}, {-3, -2}}, {{1, 1}, {-2, 3}, {-3, -2}}, {{-1, -1}, {-2, -2}, {-3, -3}}}},
                                                          {'U', {{{-1, 2}, {2, 1}, {3, 3}}, {{1, -2}, {2, 1}, {3, 3}}, {{1, -2}, {-2, -1}, {3, 3}}, {{-1, 2}, {-2, -1}, {3, 3}}, {{-1, -1}, {2, 2}, {-3, -3}}, {{1, 1}, {2, 2}, {-3, -3}}, {{1, 1}, {-2, -2}, {-3, -3}}, {{-1, -1}, {-2, -2}, {-3, -3}}}},
                                                          {'F', {{{-1, -1}, {2, 2}, {3, 3}}, {{1, 1}, {2, 2}, {3, 3}}, {{1, -3}, {-2, -2}, {3, 1}}, {{-1, 3}, {-2, -2}, {3, 1}}, {{-1, -1}, {2, 2}, {-3, -3}}, {{1, 1}, {2, 2}, {-3, -3}}, {{1, -3}, {-2, -2}, {-3, -1}}, {{-1, 3}, {-2, -2}, {-3, -1}}}},
                                                          {'B', {{{-1, -3}, {2, 2}, {3, -1}}, {{1, 3}, {2, 2}, {3, -1}}, {{1, 1}, {-2, -2}, {3, 3}}, {{-1, -1}, {-2, -2}, {3, 3}}, {{-1, -3}, {2, 2}, {-3, 1}}, {{1, 3}, {2, 2}, {-3, 1}}, {{1, 1}, {-2, -2}, {-3, -3}}, {{-1, -1}, {-2, -2}, {-3, -3}}}},
                                                          {'L', {{{-1, -1}, {2, 3}, {3, -2}}, {{1, 1}, {2, 2}, {3, 3}}, {{1, 1}, {-2, -2}, {3, 3}}, {{-1, -1}, {-2, -3}, {3, -2}}, {{-1, -1}, {2, 3}, {-3, 2}}, {{1, 1}, {2, 2}, {-3, -3}}, {{1, 1}, {-2, -2}, {-3, -3}}, {{-1, -1}, {-2, -3}, {-3, 2}}}},
                                                          {'D', {{{-1, -1}, {2, 2}, {3, 3}}, {{1, 1}, {2, 2}, {3, 3}}, {{1, 1}, {-2, -2}, {3, 3}}, {{-1, -1}, {-2, -2}, {3, 3}}, {{-1, -2}, {2, -1}, {-3, -3}}, {{1, 2}, {2, -1}, {-3, -3}}, {{1, 2}, {-2, 1}, {-3, -3}}, {{-1, -2}, {-2, 1}, {-3, -3}}}}};
vector<vector<int>> parityEdgeValues = {{1, 0, 1, 0, 0, 0, 0, 0, 1, 0, 1, 0},
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
vector<vector<int>> parityCornerValues = {{1, 0, 1, 0, 0, 1, 0, 1},
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
Cube Execute(Cube c, string s)
{
    Cube temp = c;
    for (auto i : s)
    {
        for (int j = 0; j < 12; j++)
            temp.first.second[j] = nextEdgeOrientation[i][temp.first.first[j]][temp.first.second[j]],
            temp.first.first[j] = nextEdgePos[i][temp.first.first[j]];
        for (int j = 0; j < 8; j++)
            temp.second.second[j] = nextCornerOrientation[i][temp.second.first[j]][temp.second.second[j]],
            temp.second.first[j] = nextCornerPos[i][temp.second.first[j]];
    }
    return temp;
}
vector<string> randomize(vector<string> s)
{
    mt19937 gen(chrono::system_clock::now().time_since_epoch().count());
    shuffle(s.begin(), s.end(), gen);
    return s;
}
void printStatus(Cube c)
{
    cout << "{" << c.first.first[0];
    for (int i = 1; i < 12; i++)
        cout << "," << c.first.first[i];
    cout << "}\t";
    cout << "{" << c.first.second[0];
    for (int i = 1; i < 12; i++)
        cout << "," << c.first.second[i];
    cout << "}\t";
    cout << "{" << c.second.first[0];
    for (int i = 1; i < 8; i++)
        cout << "," << c.second.first[i];
    cout << "}\t";
    cout << "{" << c.second.second[0];
    for (int i = 1; i < 8; i++)
        cout << "," << c.second.second[i];
    cout << "}\n";
}
string reverseAlgorithm(string s)
{
    string result = "";
    for (auto i : s)
        for (int j = 0; j < 3; j++)
            result += i;
    reverse(result.begin(), result.end());
    return result;
}
vector<int> maskG1Condition(Cube c)
{
    vector<pair<int, int>> edgeMask(12);
    vector<int> cornerMask(8), finalMask;
    for (int i = 0; i < 4; i++)
        edgeMask[c.first.first[i]] = {0, c.first.second[i]};
    for (int i = 4; i < 8; i++)
        edgeMask[c.first.first[i]] = {1, c.first.second[i]};
    for (int i = 8; i < 12; i++)
        edgeMask[c.first.first[i]] = {0, c.first.second[i]};
    for (int i = 0; i < 8; i++)
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
    vector<string> operations = {"R", "U", "F", "L", "B", "D"};
    queue<pair<Cube, string>> forward, backward;
    map<vector<int>, string> f_sol, b_sol;
    f_sol[maskG1Condition(source)] = b_sol[maskG1Condition(destination)] = "";
    forward.push({source, ""});
    backward.push({destination, ""});
    while (true)
    {
        auto f = forward.front();
        forward.pop();
        for (auto i : randomize(operations))
        {
            Cube temp = Execute(f.first, i);
            vector<int> x = maskG1Condition(temp);
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
        for (auto i : randomize(operations))
        {
            Cube temp = Execute(b.first, i);
            vector<int> x = maskG1Condition(temp);
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
vector<int> maskG2Condition(Cube c)
{
    vector<int> result, parityEdges(12), parityCorners(8), parityCornerPos(8);
    for (int i = 0; i < 12; i++)
        parityEdges[c.first.first[i]] = parityEdgeValues[c.first.first[i]][i];
    for (int i = 0; i < 8; i++)
        parityCorners[c.second.first[i]] = parityCornerValues[c.second.first[i]][i], parityCornerPos[c.second.first[i]] = i / 4;
    for (int i = 0; i < 4; i++)
        result.push_back(parityEdges[i]);
    for (int i = 8; i < 12; i++)
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
    vector<string> operations = {"U", "D", "RR", "FF", "LL", "BB"};
    queue<pair<Cube, string>> forward, backward;
    map<vector<int>, string> f_sol, b_sol;
    f_sol[maskG2Condition(source)] = b_sol[maskG2Condition(destination)] = "";
    forward.push({source, ""});
    backward.push({destination, ""});
    while (true)
    {
        auto f = forward.front();
        forward.pop();
        for (auto i : randomize(operations))
        {
            Cube temp = Execute(f.first, i);
            vector<int> x = maskG2Condition(temp);
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
        for (auto i : randomize(operations))
        {
            Cube temp = Execute(b.first, i);
            vector<int> x = maskG2Condition(temp);
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
vector<int> maskG3condition(Cube c)
{
    vector<int> result;
    for (int i = 0; i < 12; i++)
        result.push_back(c.first.first[i]);
    for (int i = 0; i < 8; i++)
        result.push_back(c.second.first[i]);
    return result;
}
vector<int> maskcondition(Cube c)
{
    vector<int> v;
    for (auto i : c.first.first)
        v.push_back(i);
    for (auto i : c.second.first)
        v.push_back(i);
    return v;
}
string solveG3(Cube c)
{
    Cube source = c, destination = {{SOLVED_EDGE_POS, SOLVED_EDGE_ORIENTATION}, {SOLVED_CORNER_POS, SOLVED_CORNER_ORIENTATION}};
    // vector<string> operations = {"U", "UUU", "D", "DDD", "RR", "FF", "LL", "BB"};
    vector<string> operations = {"UU", "DD", "RR", "FF", "LL", "BB"};
    queue<pair<Cube, string>> forward, backward;
    map<vector<int>, string> f_sol, b_sol;
    f_sol[maskG3condition(source)] = b_sol[maskG3condition(destination)] = "";
    forward.push({source, ""});
    backward.push({destination, ""});
    while (true)
    {
        auto f = forward.front();
        forward.pop();
        for (auto i : randomize(operations))
        {
            Cube temp = Execute(f.first, i);
            vector<int> x = maskG3condition(temp);
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
        for (auto i : randomize(operations))
        {
            Cube temp = Execute(b.first, i);
            vector<int> x = maskG3condition(temp);
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
    stack<pair<char, int>> s;
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
map<vector<int>, string> loadMapFromFile(const string &filename)
{
    map<vector<int>, string> data;
    ifstream inputFile(filename, ios::binary);
    if (inputFile.is_open())
    {
        size_t mapSize;
        inputFile.read(reinterpret_cast<char *>(&mapSize), sizeof(mapSize));
        for (size_t i = 0; i < mapSize; ++i)
        {
            size_t vectorSize;
            inputFile.read(reinterpret_cast<char *>(&vectorSize), sizeof(vectorSize));
            vector<int> keyVector(vectorSize);
            for (size_t j = 0; j < vectorSize; ++j)
            {
                int element;
                inputFile.read(reinterpret_cast<char *>(&element), sizeof(element));
                keyVector[j] = element;
            }
            string value;
            getline(inputFile, value);
            data[keyVector] = value;
        }
        inputFile.close();
    }
    else
        std::cerr << "Unable to open file: " << filename << std::endl;
    return data;
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
                for (int j = 0; j < 3; j++)
                    result += i[0];
            else
                for (int j = 0; j < 2; j++)
                    result += i[0];
        }
    }
    return result;
}
void inputCubeStats(Cube &c)
{
    for (int i = 0; i < 12; i++)
        cin >> c.first.first[i] >> c.first.second[i]; // To input edge positions and orientation
    for (int i = 0; i < 8; i++)
        cin >> c.second.first[i] >> c.second.second[i]; // To input corner position and orientation
}
// U - 4
// L - 13
// F - 22
// R - 31
// B - 40
// D - 49

// ULFRBD
//  map<int,bitset<6>>edgePosForNumber={{0,0b100010},{1,0b100100},{2,0b101000},{3,0b110000},{4,}};
//  map<int,bitset<6>>edgePosForNumber={{0,0b110010},{1,0b100110},{2,0b1}};
//  Colors would map onto the centers ULFRBD and so on, the centers would then map to the edgeNumber, vice versa, the
Cube colorToMatrix(string colorInput)
{
    map<char, char> colorToPos, posToColor;
    string basicPositions = "ULFRBD", givenPositions = "";
    vector<int> basicPositionsInfo = {4, 13, 22, 31, 40, 49};
    for (auto i : basicPositionsInfo)
        givenPositions += colorInput[i];
    for (int i = 0; i < 6; i++)
        posToColor[basicPositions[i]] = givenPositions[i], colorToPos[givenPositions[i]] = basicPositions[i];
}
int main()
{
    // map<vector<int>, string> G3 = loadMapFromFile("pruneG3_optimum.bin");
    while(true){
    string input;
    // cin>>input;
    getline(cin, input);
    // for (auto i : splitString(input))
    // cout << i << " ";
    // for (auto i : alg(input))
    //     cout << i << " ";
    cout << endl;
    cout << endl;
    // return 0;
    input = ip(splitString(input));
    Cube c = {{SOLVED_EDGE_POS, SOLVED_EDGE_ORIENTATION}, {SOLVED_CORNER_POS, SOLVED_CORNER_ORIENTATION}};
    // Cube c = {{{5, 3, 4, 9, 8, 11, 2, 6, 7, 10, 1, 0}, {1, -1, 2, 1, 2, -3, -2, 1, -1, -3, 3, 2}}, {{3, 7, 6, 5, 4, 2, 0, 1}, {3, -2, 1, 1, -1, 1, 3, 2}}};
    // inputCubeStats(c);
    c = Execute(c, input);
    string G0_to_G1 = solveG1(c);
    for (auto i : alg(G0_to_G1))
        cout << i << " ";
    // cout << G0_to_G1;
    cout << endl;
    c = Execute(c, G0_to_G1);
    string G1_to_G2 = solveG2(c);
    for (auto i : alg(G1_to_G2))
        cout << i << " ";
    cout << endl;
    // string G1_to_G2 = "";
    c = Execute(c, G1_to_G2);
    // string G2_to_G3 = G3[maskcondition(c)];
    string G2_to_G3 = solveG3(c);
    for (auto i : alg(G2_to_G3))
        cout << i << " ";
    cout << endl;
    // string G2_to_G3 = "";
    vector<string> output = alg(G0_to_G1 + G1_to_G2 + G2_to_G3);
    for (auto i : output)
        cout << i << " ";
    // // cout << solveG2(c);
    cout << endl
         << output.size() << endl;}
}
// LLFFUULLBBUUULLUUFFLLDFFFLRRDDDURRRBBFFFLLBB
// RRDDRRURRRBBBLLLRRRBBBFFFRRR
// BBUBDDFDDRRBBBLLFRRFUUUFFFUBBBLBBLL
// works for
// RRLLLBBBLDDDFDDFFLLLDDLLUUULLFFBBUUUBBRRDDRRU
// DDLDDLLUFRLBBBUULLUUUBBRRFFUFFDFFBB
// LBBBLLRRBBRRDDFFFDDFRRUUBDDDLLLBBURRFFFDDUU
// FFUUUDDBLLDDFLLBBUURRDDFFFULLLFLDDDUUF
// BBBLLRRUUFFFLLBBBUULLFFFRRLLLBBFDULLLFRRRFD

// BDDBBBUURRFFFRRFFFUUFFUUULFFDFRRRUUBBFUU