import React from 'react';
import { Settings, Music, Brain, Heart } from 'lucide-react';

const CategoryTabs = ({ activeTab, setActiveTab, categories }) => {
  const tabIcons = {
    custom: Settings,
    therapeutic: Heart,
    musical: Music,
    binaural: Brain,
    brainwaves: Brain
  };

  return (
    <div className="p-4 border-b">
      <div className="grid grid-cols-2 gap-2">
        {categories.map((category) => {
          const Icon = tabIcons[category];
          return (
            <button
              key={category}
              onClick={() => setActiveTab(category)}
              className={`flex items-center justify-center gap-2 py-3 px-2 rounded-xl border-2 transition-all active:scale-95 ${
                activeTab === category
                  ? 'bg-purple-500 text-white border-purple-500 shadow-md'
                  : 'bg-white text-gray-700 border-gray-300'
              }`}
            >
              <Icon className="w-4 h-4" />
              <span className="text-xs font-medium">{category.charAt(0).toUpperCase() + category.slice(1)}</span>
            </button>
          );
        })}
      </div>
    </div>
  );
};

export default CategoryTabs;