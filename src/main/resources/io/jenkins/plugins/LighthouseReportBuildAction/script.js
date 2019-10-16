import React from 'react';
import { render } from 'react-dom';
import ReportViewer from 'react-lighthouse-viewer';

const App = () => (
    <ReportViewer json={window.__LIGHTHOUSE_JSON__} />
);
render(<App />, document.getElementById("root"));
