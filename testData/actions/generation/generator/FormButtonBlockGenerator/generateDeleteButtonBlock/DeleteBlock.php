<?php

namespace Foo\Bar\Block\Form\Book;

use Magento\Framework\View\Element\UiComponent\Control\ButtonProviderInterface;

/**
 * Delete entity button.
 */
class DeleteBlock extends GenericButton implements ButtonProviderInterface
{
    /**
     * Retrieve Delete button settings.
     *
     * @return array
     */
    public function getButtonData(): array
    {
        return $this->wrapButtonSettings(
            'Delete',
            'delete',
            'deleteConfirm(\''
            . __('Are you sure you want to delete this book?')
            . '\', \'' . $this->getUrl('*/*/delete', ['book_id' => $this->getBookId()]) . '\')',
            [],
            20
        );
    }
}
