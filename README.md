# StableMatchingProblem

The situation is the following: There are n students aiming to secure one of m Summer 2020 internship opportunities. Each student has a ranking of the internships in order of preference. Each company prefers students based on their GPA, number of projects, and months of experience. However, each company will weigh these factors differently. There may be more students seeking internships than there are internship positions. We aim to assign each student to at most one internship, in such a way that all available internships are filled. (Since there may be a surplus of students, there would be some students who do not get an internship.)
We say that an assignment of students to internships is stable if neither of the following situations arises:

• First type of instability: There are students s and s′, and an internship I, such that

– s is assigned to I, and

– s′ is assigned to no internship, and 

– I prefers s′ to s

• Second type of instability: There are students s and s′, and internships I and I′, so that

– s is assigned to I, and 

– s′ is assigned to I′, and 

– I prefers s′ to s, and

– s prefers I to I'
