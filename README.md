# lighthouse-report-plugin

A simple plugin that displays a lighthouse report inside jenkins.

You can use archive() function to save files, but you cannot view any html with javascript for security reasons.

This will keep the html inside the plugin, but the data is pull in from your build result.

Report Name can also be specified to generate Lighthouse report with a specific name, supports multiple reports.

## Usage

```groovy
// Generate your lighthouse report.
node {
    sh label:'Run Lighthouse', script:'npx lighthouse-ci https://www.example.com --jsonReport --report=.'
    lighthouseReport('./report.json')
}

//Generate your lighthouse report with specific report name
node {
    sh label:'Run Lighthouse', script:'npx lighthouse-ci https://www.example.com --jsonReport --report=.'
    lighthouseReport file: './report.json', name: 'My Report'
}



## Screenshot

![Screenshot](./screenshot.png)
