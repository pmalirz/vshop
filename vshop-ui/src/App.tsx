import React from 'react';
import './App.css';
import ProductSearch from './components/ProductSearch/ProductSearch';
import ProductGenerator from './components/ProductGenerator/ProductGenerator';

const App: React.FC = () => {
    return (
        <div className="app">
            <div className="component">
                <ProductGenerator/>
            </div>
            <div className="component">
                <ProductSearch/>
            </div>
        </div>
    );
};

export default App;
