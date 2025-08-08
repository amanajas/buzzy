import React from 'react';
import { Headphones } from 'lucide-react';

const Header = () => {
  return (
    <div className="bg-gradient-to-r from-purple-600 to-blue-600 text-white p-6 text-center">
      <div className="flex items-center justify-center gap-2 mb-2">
        <Headphones className="w-7 h-7" />
        <h1 className="text-xl font-bold">Buzzy</h1>
      </div>
      <p className="text-purple-100 text-sm">Custom sound therapy & binaural beats</p>
      <div className="mt-3 p-2 bg-yellow-400 bg-opacity-20 rounded-lg">
        <p className="text-xs text-yellow-100">
          🎧 Use headphones for best experience
        </p>
      </div>
    </div>
  );
};

export default Header;