import io
import sys
import json


def main():
    with open(sys.argv[1], 'r') as read_file:
        parameter_json = json.load(read_file)

        # Creating an HTML file
        html_file = io.StringIO("")
        # Adding input data to the HTML file
        html_file.write("<html>\n<head>\n<title>HTML from Job ID " + str(parameter_json["id"]) + "</title>\n")
        html_file.write("<style>\nimg {\ndisplay: block;\nmargin-left: auto;\nmargin-right: auto;\n}\n")
        html_file.write("h1,h2,p{ text-align: center; }\n</style>")
        html_file.write("</head>\n<body>\n<h1><center>" + parameter_json["title"] + "</center></h1>\n")
        html_file.write(
            "<img src=\"data:image/jpeg;base64," + parameter_json["image"] + "\" style=\"width:50%;\"></img>")
        html_file.write("<h2>" + parameter_json["description"] + "</h2>\n")
        html_file.write("<p>Published at: " + parameter_json["publishedAt"] + "</p>")
        html_file.write("</body>\n</html>")
        html_file.seek(0)
        print(html_file.read())

        html_file.close()


if __name__ == "__main__":
    main()
