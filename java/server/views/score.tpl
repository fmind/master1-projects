<!DOCTYPE html>
<html lang="fr">
<head>
    <title>TOP SCORE - Captain Duke</title>
    <meta charset="utf-8" />

    <style type="text/css">
        body {
            height: 100%;
            background: url("img/saturn.jpg");
            text-align: center;
            color: white;
        }

        h1, table {
            display: inline-block;
        }

        h1 {
            color: green;
        }

        th, td {
            width: 200px;
            padding-bottom: 10px;
        }

        th {
            color: lime;
        }
    </style>
</head>
<body>
    <h1>TOP SCORE - Captain Duke</h1>
    <br />

    <table>
        <tr>
            <th>Numéro</th>
            <th>Nom</th>
            <th>Score</th>
            <th>Date</th>
        </tr>
        %i=0
        %for name, score, date in scores:
            %i += 1
            <tr>
                <td>{{i}}</td>
                <td>{{name}}</td>
                <td>{{score}}</td>
                <td>{{date}}</td>
            </tr>
        %end
    </table>
</body>
